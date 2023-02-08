package com.fasterxml.jackson.core;

import java.io.IOException;

/**
 * Interface that defines objects that can read and write
 * {@link TreeNode} instances using Streaming API.
 *
 * @since 2.3
 */
 abstract class TreeCodec
{
     abstract <T extends TreeNode> T readTree(JsonParser p) throws IOException, JsonProcessingException;
     abstract void writeTree(JsonGenerator g, TreeNode tree) throws IOException, JsonProcessingException;

    /**
     * @return Node that represents "missing" node during traversal: something
     *   referenced but that does not exist in content model
     *
     * @since 2.10
     */
     TreeNode missingNode() {
        return null;
    }

    /**
     * @return Node that represents explicit {@code null} value in content
     *
     * @since 2.10
     */
     TreeNode nullNode() {
        return null;
    }

     abstract TreeNode createArrayNode();
     abstract TreeNode createObjectNode();
     abstract JsonParser treeAsTokens(TreeNode node);
}
