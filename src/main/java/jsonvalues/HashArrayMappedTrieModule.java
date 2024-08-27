package jsonvalues;

import static java.lang.Integer.bitCount;
import static jsonvalues.HashArrayMappedTrieModule.Action.PUT;
import static jsonvalues.HashArrayMappedTrieModule.Action.REMOVE;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

interface HashArrayMappedTrieModule {

  enum Action {
    PUT, REMOVE
  }

  class LeafNodeIterator implements Iterator<LeafNode> {

    // buckets levels + leaf level = (Integer.SIZE / AbstractNode.SIZE + 1) + 1
    private final static int MAX_LEJsValueELS = Integer.SIZE / AbstractNode.SIZE + 2;

    private final int total;
    private final Object[] nodes = new Object[MAX_LEJsValueELS];
    private final int[] indexes = new int[MAX_LEJsValueELS];

    private int level;
    private int ptr = 0;

    LeafNodeIterator(AbstractNode root) {
      total = root.size();
      level = downstairs(nodes,
                         indexes,
                         root,
                         0);
    }

    private static int downstairs(Object[] nodes,
                                  int[] indexes,
                                  AbstractNode root,
                                  int level) {
      while (true) {
        nodes[level] = root;
        indexes[level] = 0;
        root = getChild(root,
                        0);
        if (root == null) {
          break;
        } else {
          level++;
        }
      }
      return level;
    }

    private static AbstractNode getChild(AbstractNode node,
                                         int index) {
      if (node instanceof IndexedNode indexedNode) {
        final Object[] subNodes = indexedNode.subNodes;
        return index < subNodes.length ? (AbstractNode) subNodes[index] : null;
      } else if (node instanceof ArrayNode arrayNode) {
        return index < AbstractNode.BUCKET_SIZE ? (AbstractNode) arrayNode.subNodes[index] : null;
      }
      return null;
    }

    @Override
    public boolean hasNext() {
      return ptr < total;
    }

    @Override
    public LeafNode next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Object node = nodes[level];
      while (!(node instanceof LeafNode)) {
        node = findNextLeaf();
      }
      ptr++;
      if (node instanceof final LeafList leaf) {
        nodes[level] = leaf.tail;
        return leaf;
      } else {
        nodes[level] = EmptyNode.instance();
        return (LeafSingleton) node;
      }
    }

    private Object findNextLeaf() {
      AbstractNode node = null;
      while (level > 0) {
        level--;
        indexes[level]++;
        node = getChild((AbstractNode) nodes[level],
                        indexes[level]);
        if (node != null) {
          break;
        }
      }
      level = downstairs(nodes,
                         indexes,
                         node,
                         level + 1);
      return nodes[level];
    }
  }

  /**
   * An abstract base class for nodes of a HAMT.
   */
  abstract class AbstractNode implements HashArrayMappedTrie {

    static final int SIZE = 5;
    static final int BUCKET_SIZE = 1 << SIZE;
    static final int MAX_INDEX_NODE = BUCKET_SIZE >> 1;
    static final int MIN_ARRAY_NODE = BUCKET_SIZE >> 2;

    static int hashFragment(int shift,
                            int hash) {
      return (hash >>> shift) & (BUCKET_SIZE - 1);
    }

    static int toBitmap(int hash) {
      return 1 << hash;
    }

    static int fromBitmap(int bitmap,
                          int bit) {
      return bitCount(bitmap & (bit - 1));
    }

    static Object[] update(Object[] arr,
                           int index,
                           Object newElement) {
      final Object[] newArr = Arrays.copyOf(arr,
                                            arr.length);
      newArr[index] = newElement;
      return newArr;
    }

    static Object[] remove(Object[] arr,
                           int index) {
      final Object[] newArr = new Object[arr.length - 1];
      System.arraycopy(arr,
                       0,
                       newArr,
                       0,
                       index);
      System.arraycopy(arr,
                       index + 1,
                       newArr,
                       index,
                       arr.length - index - 1);
      return newArr;
    }

    static Object[] insert(Object[] arr,
                           int index,
                           Object newElem) {
      final Object[] newArr = new Object[arr.length + 1];
      System.arraycopy(arr,
                       0,
                       newArr,
                       0,
                       index);
      newArr[index] = newElem;
      System.arraycopy(arr,
                       index,
                       newArr,
                       index + 1,
                       arr.length - index);
      return newArr;
    }

    abstract Optional<JsValue> lookup(int shift,
                                      int keyHash,
                                      String key);

    abstract JsValue lookup(int shift,
                            int keyHash,
                            String key,
                            JsValue defaultJsValuealue);

    abstract AbstractNode modify(int shift,
                                 int keyHash,
                                 String key,
                                 JsValue value,
                                 Action action);

    Iterator<LeafNode> nodes() {
      return new LeafNodeIterator(this);
    }

    @Override
    public Iterator<LeafNode> iterator() {
      return nodes();

    }

    @Override
    public Iterator<String> keysIterator() {
      return HashArrayMappedTrie.map(nodes(),
                                     LeafNode::key);

    }

    @Override
    public Iterator<JsValue> valuesIterator() {
      return HashArrayMappedTrie.map(nodes(),
                                     LeafNode::value);
    }

    @Override
    public Optional<JsValue> get(String key) {
      return lookup(0,
                    Objects.hashCode(key),
                    key);
    }

    @Override
    public JsValue getOrElse(String key,
                             JsValue value) {
      return lookup(0,
                    Objects.hashCode(key),
                    key,
                    value);
    }

    @Override
    public boolean containsKey(String key) {
      return get(key).isPresent();
    }

    @Override
    public HashArrayMappedTrie put(String key,
                                   JsValue value) {
      return modify(0,
                    Objects.hashCode(key),
                    key,
                    value,
                    PUT);
    }

    @Override
    public HashArrayMappedTrie remove(String key) {
      return modify(0,
                    Objects.hashCode(key),
                    key,
                    null,
                    REMOVE);
    }


  }

  /**
   * The empty node.
   */
  @SuppressWarnings("serial")
  final class EmptyNode extends AbstractNode implements Serializable {

    private static final EmptyNode INSTANCE = new EmptyNode();

    private EmptyNode() {
    }

    static EmptyNode instance() {
      return INSTANCE;
    }

    @Override
    Optional<JsValue> lookup(int shift,
                             int keyHash,
                             String key) {
      return Optional.empty();
    }

    @Override
    JsValue lookup(int shift,
                   int keyHash,
                   String key,
                   JsValue defaultJsValuealue) {
      return defaultJsValuealue;
    }

    @Override
    AbstractNode modify(int shift,
                        int keyHash,
                        String key,
                        JsValue value,
                        Action action) {
      return (action == REMOVE) ? this : new LeafSingleton(keyHash,
                                                           key,
                                                           value);
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public Iterator<LeafNode> nodes() {
      return new Iterator<>() {
        @Override
        public boolean hasNext() {
          return false;
        }

        @Override
        public LeafNode next() {
          throw new NoSuchElementException();
        }
      };
    }

  }

  /**
   * Representation of a HAMT leaf.
   */
  abstract class LeafNode extends AbstractNode {

    static AbstractNode mergeLeaves(int shift,
                                    LeafNode leaf1,
                                    LeafSingleton leaf2) {
      final int h1 = leaf1.hash();
      final int h2 = leaf2.hash();
      if (h1 == h2) {
        return new LeafList(h1,
                            leaf2.key(),
                            leaf2.value(),
                            leaf1);
      }
      final int subH1 = hashFragment(shift,
                                     h1);
      final int subH2 = hashFragment(shift,
                                     h2);
      final int newBitmap = toBitmap(subH1) | toBitmap(subH2);
      if (subH1 == subH2) {
        final AbstractNode newLeaves = mergeLeaves(shift + SIZE,
                                                   leaf1,
                                                   leaf2);
        return new IndexedNode(newBitmap,
                               newLeaves.size(),
                               new Object[]{newLeaves});
      } else {
        return new IndexedNode(newBitmap,
                               leaf1.size() + leaf2.size(),
                               subH1 < subH2 ? new Object[]{leaf1, leaf2} : new Object[]{leaf2, leaf1}
        );
      }
    }

    public abstract String key();

    public abstract JsValue value();

    abstract int hash();

    @Override
    public boolean isEmpty() {
      return false;
    }
  }

  /**
   * Representation of a HAMT leaf node with single element.
   */
  final class LeafSingleton extends LeafNode {


    private final int hash;
    private final String key;
    private final JsValue value;

    LeafSingleton(int hash,
                  String key,
                  JsValue value) {
      this.hash = hash;
      this.key = key;
      this.value = value;
    }

    private boolean equals(int keyHash,
                           String key) {
      return keyHash == hash && Objects.equals(key,
                                               this.key);
    }

    @Override
    Optional<JsValue> lookup(int shift,
                             int keyHash,
                             String key) {
      return equals(keyHash,
                    key) ? Optional.of(value) : Optional.empty();
    }

    @Override
    JsValue lookup(int shift,
                   int keyHash,
                   String key,
                   JsValue defaultValue) {
      return equals(keyHash,
                    key) ? value : defaultValue;
    }

    @Override
    AbstractNode modify(int shift,
                        int keyHash,
                        String key,
                        JsValue value,
                        Action action) {
      if (keyHash == hash && Objects.equals(key,
                                            this.key)) {
        return (action == REMOVE) ? EmptyNode.instance() : new LeafSingleton(hash,
                                                                             key,
                                                                             value);
      } else {
        return (action == REMOVE) ? this : mergeLeaves(shift,
                                                       this,
                                                       new LeafSingleton(keyHash,
                                                                         key,
                                                                         value));
      }
    }

    @Override
    public int size() {
      return 1;
    }

    @Override
    public Iterator<LeafNode> nodes() {
      return new Iterator<>() {

        boolean hasNext = true;

        @Override
        public boolean hasNext() {
          return hasNext;
        }

        @Override
        public LeafNode next() {
          hasNext = false;
          return LeafSingleton.this;
        }
      };
    }

    @Override
    int hash() {
      return hash;
    }

    @Override
    public String key() {
      return key;
    }

    @Override
    public JsValue value() {
      return value;
    }
  }

  /**
   * Representation of a HAMT leaf node with more than one element.
   */
  @SuppressWarnings("serial")
  final class LeafList extends LeafNode implements Serializable {

    private final int hash;
    private final String key;
    private final JsValue value;
    private final int size;
    private final LeafNode tail;

    LeafList(int hash,
             String key,
             JsValue value,
             LeafNode tail) {
      this.hash = hash;
      this.key = key;
      this.value = value;
      this.size = 1 + tail.size();
      this.tail = tail;
    }

    private static AbstractNode mergeNodes(LeafNode leaf1,
                                           LeafNode leaf2) {
      if (leaf2 == null) {
        return leaf1;
      }
      if (leaf1 instanceof LeafSingleton) {
        return new LeafList(leaf1.hash(),
                            leaf1.key(),
                            leaf1.value(),
                            leaf2);
      }
      if (leaf2 instanceof LeafSingleton) {
        return new LeafList(leaf2.hash(),
                            leaf2.key(),
                            leaf2.value(),
                            leaf1);
      }
      LeafNode result = leaf1;
      LeafNode tail = leaf2;
      while (tail instanceof LeafList list) {
        result = new LeafList(list.hash,
                              list.key,
                              list.value,
                              result);
        tail = list.tail;
      }
      return new LeafList(tail.hash(),
                          tail.key(),
                          tail.value(),
                          result);
    }

    @Override
    Optional<JsValue> lookup(int shift,
                             int keyHash,
                             String key) {
      if (hash != keyHash) {
        return Optional.empty();
      }
      var iter = nodes();
      while (iter.hasNext()) {
        LeafNode next = iter.next();
        if (Objects.equals(next.key(),
                           key)) {
          return Optional.of(next.value());
        }
      }
      return Optional.empty();
    }

    @Override
    JsValue lookup(int shift,
                   int keyHash,
                   String key,
                   JsValue defaultJsValuealue) {
      if (hash != keyHash) {
        return defaultJsValuealue;
      }
      JsValue result = defaultJsValuealue;
      final Iterator<LeafNode> iterator = nodes();
      while (iterator.hasNext()) {
        final LeafNode node = iterator.next();
        if (Objects.equals(node.key(),
                           key)) {
          result = node.value();
          break;
        }
      }
      return result;
    }

    @Override
    AbstractNode modify(int shift,
                        int keyHash,
                        String key,
                        JsValue value,
                        Action action) {
      if (keyHash == hash) {
        final AbstractNode filtered = removeElement(key);
        if (action == REMOVE) {
          return filtered;
        } else {
          return new LeafList(hash,
                              key,
                              value,
                              (LeafNode) filtered);
        }
      } else {
        return (action == REMOVE) ? this : mergeLeaves(shift,
                                                       this,
                                                       new LeafSingleton(keyHash,
                                                                         key,
                                                                         value));
      }
    }

    private AbstractNode removeElement(String k) {
      if (Objects.equals(k,
                         this.key)) {
        return tail;
      }
      LeafNode leaf1 = new LeafSingleton(hash,
                                         key,
                                         value);
      LeafNode leaf2 = tail;
      boolean found = false;
      while (!found && leaf2 != null) {
        if (Objects.equals(k,
                           leaf2.key())) {
          found = true;
        } else {
          leaf1 = new LeafList(leaf2.hash(),
                               leaf2.key(),
                               leaf2.value(),
                               leaf1);
        }
        leaf2 = leaf2 instanceof LeafList ? ((LeafList) leaf2).tail : null;
      }
      return mergeNodes(leaf1,
                        leaf2);
    }

    @Override
    public int size() {
      return size;
    }

    @Override
    public Iterator<LeafNode> nodes() {
      return new Iterator<>() {
        LeafNode node = LeafList.this;

        @Override
        public boolean hasNext() {
          return node != null;
        }

        @Override
        public LeafNode next() {
          if (!hasNext()) {
            throw new NoSuchElementException();
          }
          final LeafNode result = node;
          if (node instanceof LeafSingleton) {
            node = null;
          } else {
            node = ((LeafList) node).tail;
          }
          return result;
        }
      };
    }

    @Override
    int hash() {
      return hash;
    }

    @Override
    public String key() {
      return key;
    }

    @Override
    public JsValue value() {
      return value;
    }
  }

  /**
   * Representation of a HAMT indexed node.
   */
  final class IndexedNode extends AbstractNode {


    private final int bitmap;
    private final int size;
    private final Object[] subNodes;

    IndexedNode(int bitmap,
                int size,
                Object[] subNodes) {
      this.bitmap = bitmap;
      this.size = size;
      this.subNodes = subNodes;
    }

    @Override
    Optional<JsValue> lookup(int shift,
                             int keyHash,
                             String key) {
      final int frag = hashFragment(shift,
                                    keyHash);
      final int bit = toBitmap(frag);
      if ((bitmap & bit) != 0) {
        final AbstractNode n = (AbstractNode) subNodes[fromBitmap(bitmap,
                                                                  bit)];
        return n.lookup(shift + SIZE,
                        keyHash,
                        key);
      } else {
        return Optional.empty();
      }
    }

    @Override
    JsValue lookup(int shift,
                   int keyHash,
                   String key,
                   JsValue defaultJsValuealue) {
      final int frag = hashFragment(shift,
                                    keyHash);
      final int bit = toBitmap(frag);
      if ((bitmap & bit) != 0) {
        final AbstractNode n = (AbstractNode) subNodes[fromBitmap(bitmap,
                                                                  bit)];
        return n.lookup(shift + SIZE,
                        keyHash,
                        key,
                        defaultJsValuealue);
      } else {
        return defaultJsValuealue;
      }
    }

    @Override
    AbstractNode modify(int shift,
                        int keyHash,
                        String key,
                        JsValue value,
                        Action action) {
      final int frag = hashFragment(shift,
                                    keyHash);
      final int bit = toBitmap(frag);
      final int index = fromBitmap(bitmap,
                                   bit);
      final int mask = bitmap;
      final boolean exists = (mask & bit) != 0;
      final AbstractNode atIndx = exists ? (AbstractNode) subNodes[index] : null;
      final AbstractNode child =
          exists ? atIndx.modify(shift + SIZE,
                                 keyHash,
                                 key,
                                 value,
                                 action)
                 : EmptyNode.instance()
                            .modify(shift + SIZE,
                                    keyHash,
                                    key,
                                    value,
                                    action);
      final boolean removed = exists && child.isEmpty();
      final boolean added = !exists && !child.isEmpty();
      final int newBitmap = removed ? mask & ~bit : added ? mask | bit : mask;
      if (newBitmap == 0) {
        return EmptyNode.instance();
      } else if (removed) {
        if (subNodes.length <= 2 && subNodes[index ^ 1] instanceof LeafNode) {
          return (AbstractNode) subNodes[index ^ 1]; // collapse
        } else {
          return new IndexedNode(newBitmap,
                                 size - atIndx.size(),
                                 remove(subNodes,
                                        index));
        }
      } else if (added) {
        if (subNodes.length >= MAX_INDEX_NODE) {
          return expand(frag,
                        child,
                        mask,
                        subNodes);
        } else {
          return new IndexedNode(newBitmap,
                                 size + child.size(),
                                 insert(subNodes,
                                        index,
                                        child));
        }
      } else {
        if (!exists) {
          return this;
        } else {
          return new IndexedNode(newBitmap,
                                 size - atIndx.size() + child.size(),
                                 update(subNodes,
                                        index,
                                        child));
        }
      }
    }

    private ArrayNode expand(int frag,
                             AbstractNode child,
                             int mask,
                             Object[] subNodes) {
      int bit = mask;
      int count = 0;
      int ptr = 0;
      final Object[] arr = new Object[BUCKET_SIZE];
      for (int i = 0; i < BUCKET_SIZE; i++) {
        if ((bit & 1) != 0) {
          arr[i] = subNodes[ptr++];
          count++;
        } else if (i == frag) {
          arr[i] = child;
          count++;
        } else {
          arr[i] = EmptyNode.instance();
        }
        bit = bit >>> 1;
      }
      return new ArrayNode(count,
                           size + child.size(),
                           arr);
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public int size() {
      return size;
    }
  }

  /**
   * Representation of a HAMT array node.
   */
  final class ArrayNode extends AbstractNode {


    private final Object[] subNodes;
    private final int count;
    private final int size;

    ArrayNode(int count,
              int size,
              Object[] subNodes) {
      this.subNodes = subNodes;
      this.count = count;
      this.size = size;
    }

    @Override
    Optional<JsValue> lookup(int shift,
                             int keyHash,
                             String key) {
      final int frag = hashFragment(shift,
                                    keyHash);
      final AbstractNode child = (AbstractNode) subNodes[frag];
      return child.lookup(shift + SIZE,
                          keyHash,
                          key);
    }

    @Override
    JsValue lookup(int shift,
                   int keyHash,
                   String key,
                   JsValue defaultJsValuealue) {
      final int frag = hashFragment(shift,
                                    keyHash);
      final AbstractNode child = (AbstractNode) subNodes[frag];
      return child.lookup(shift + SIZE,
                          keyHash,
                          key,
                          defaultJsValuealue);
    }

    @Override
    AbstractNode modify(int shift,
                        int keyHash,
                        String key,
                        JsValue value,
                        Action action) {
      final int frag = hashFragment(shift,
                                    keyHash);
      final AbstractNode child = (AbstractNode) subNodes[frag];
      final AbstractNode newChild = child.modify(shift + SIZE,
                                                 keyHash,
                                                 key,
                                                 value,
                                                 action);
      if (child.isEmpty() && !newChild.isEmpty()) {
        return new ArrayNode(count + 1,
                             size + newChild.size(),
                             update(subNodes,
                                    frag,
                                    newChild));
      } else if (!child.isEmpty() && newChild.isEmpty()) {
        if (count - 1 <= MIN_ARRAY_NODE) {
          return pack(frag,
                      subNodes);
        } else {
          return new ArrayNode(count - 1,
                               size - child.size(),
                               update(subNodes,
                                      frag,
                                      EmptyNode.instance()));
        }
      } else {
        return new ArrayNode(count,
                             size - child.size() + newChild.size(),
                             update(subNodes,
                                    frag,
                                    newChild));
      }
    }

    private IndexedNode pack(int idx,
                             Object[] elements) {
      final Object[] arr = new Object[count - 1];
      int bitmap = 0;
      int size = 0;
      int ptr = 0;
      for (int i = 0; i < BUCKET_SIZE; i++) {
        final AbstractNode elem = (AbstractNode) elements[i];
        if (i != idx && !elem.isEmpty()) {
          size += elem.size();
          arr[ptr++] = elem;
          bitmap = bitmap | (1 << i);
        }
      }
      return new IndexedNode(bitmap,
                             size,
                             arr);
    }

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public int size() {
      return size;
    }
  }
}
