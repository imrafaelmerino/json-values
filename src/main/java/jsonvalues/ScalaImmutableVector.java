package jsonvalues;

import scala.Tuple2;
import scala.collection.JavaConverters;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.mutable.Builder;

final class ScalaImmutableVector implements ImmutableSeq
{

    private static final Vector<JsElem> EMPTY_VECTOR = new scala.collection.immutable.Vector<>(0,
                                                                                               0,
                                                                                               0
    );
    private static final CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>> bf = new CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>>()
    {
        @Override
        public Builder<JsElem, Vector<JsElem>> apply()
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }

        @Override
        public Builder<JsElem, Vector<JsElem>> apply(final Vector<JsElem> v)
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }
    };

    private final Vector<JsElem> vector;

    ScalaImmutableVector()
    {
        this.vector = EMPTY_VECTOR;
    }

    ScalaImmutableVector(final Vector<JsElem> vector)
    {
        this.vector = vector;
    }

    @Override
    public ScalaImmutableVector appendBack(final JsElem elem)
    {
        return new ScalaImmutableVector(vector.appendBack(elem));
    }

    @Override
    public ScalaImmutableVector appendFront(final JsElem elem)
    {
        return new ScalaImmutableVector(vector.appendFront(elem));
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return vector.contains(e);
    }

    @Override
    public JsElem get(final int index)
    {
        return vector.apply(index);
    }

    @Override
    public int hashCode()
    {
        return vector.hashCode();
    }

    @Override
    public JsElem head()
    {
        if (this.isEmpty()) throw UserError.headOfEmptyArr();

        return vector.head();
    }

    @Override
    public ScalaImmutableVector init()
    {
        if (this.isEmpty()) throw UserError.initOfEmptyArr();

        return new ScalaImmutableVector(vector.init());
    }

    @Override
    public boolean isEmpty()
    {
        return vector.isEmpty();
    }

    @Override
    public java.util.Iterator<JsElem> iterator()
    {
        return JavaConverters.asJavaIterator(vector.iterator()
                                                   .toIterator());
    }

    @Override
    public JsElem last()
    {
        if (this.isEmpty()) throw UserError.lastOfEmptyArr();

        return vector.last();
    }

    @Override
    public String toString()
    {
        if (vector.isEmpty()) return "[]";

        return vector.mkString("[",
                               ",",
                               "]"
                              );
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaImmutableVector remove(final int index)
    {
        if (index == 0) return new ScalaImmutableVector(vector.tail());
        if (index == vector.size() - 1) return new ScalaImmutableVector(vector.init());

        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaImmutableVector(tuple._1.$plus$plus(tuple._2.tail(),
                                                            bf
                                                           ));
    }

    @Override
    public ScalaImmutableVector add(final int index,
                                    final JsElem ele
                                   )
    {

        if (index == 0) return new ScalaImmutableVector(vector.appendFront(ele));
        if (index == -1 || index == vector.size()) return new ScalaImmutableVector(vector.appendBack(ele));
        if (index < -1 || index > vector.size()) throw UserError.indexOutOfBounds(vector.size(),
                                                                                  index,
                                                                                  "add"
                                                                                 );
        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaImmutableVector(tuple._1.appendBack(ele)
                                                .$plus$plus(tuple._2,
                                                            bf
                                                           ));
    }

    @Override
    public int size()
    {
        return vector.size();
    }


    @Override
    public ScalaImmutableVector tail()
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyArr();

        return new ScalaImmutableVector(vector.tail());
    }

    @Override
    public ScalaImmutableVector update(final int index,
                                       final JsElem ele
                                      )
    {
        return new ScalaImmutableVector(vector.updateAt(index,
                                                        ele
                                                       ));
    }


}
