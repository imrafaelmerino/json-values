/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * THIS CLASS IS INTENDED TO BE USED INTERNALLY ONLY!
 * <p>
 * This helper class provides methods that return views on Java collections.
 * The view creation and back conversion take O(1).
 */
class JavaConverters {

    private JavaConverters() {
    }



    enum ChangePolicy {

        IMMUTABLE, MUTABLE;

        boolean isMutable() {
            return this == MUTABLE;
        }
    }

    // -- private view implementations

    /**
     * Encapsulates the access to delegate and performs mutability checks.
     *
     * @param <C> The Vavr collection type
     */
    private static abstract class HasDelegate<C extends Traversable<?>> implements Serializable {

        private static final long serialVersionUID = 1L;

        private C delegate;
        private final boolean mutable;

        HasDelegate(C delegate, boolean mutable) {
            this.delegate = delegate;
            this.mutable = mutable;
        }

        protected boolean isMutable() {
            return mutable;
        }

        C getDelegate() {
            return delegate;
        }

        protected boolean setDelegateAndCheckChanged(Supplier<C> delegate) {
            ensureMutable();
            final C previousDelegate = this.delegate;
            final C newDelegate = delegate.get();
            final boolean changed = newDelegate.size() != previousDelegate.size();
            if (changed) {
                this.delegate = newDelegate;
            }
            return changed;
        }

        protected void setDelegate(Supplier<C> newDelegate) {
            ensureMutable();
            this.delegate = newDelegate.get();
        }

        protected void ensureMutable() {
            if (!mutable) {
                throw new UnsupportedOperationException();
            }
        }
    }

}
