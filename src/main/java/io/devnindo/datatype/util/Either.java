/*
 * Copyright 2023 devnindo
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
package io.devnindo.datatype.util;

public interface Either<L, R> {


    // public L left();


    public static <L, R> Either<L, R> left(L l) {
        return new LeftE<>(l);
    }

    public static <L, R> Either<L, R> right(R r) {
        return new RightE<>(r);
    }

    public boolean isLeft();

    public boolean isRight();

    public L left();

    public R right();

    public String stringPrefix();

    final class LeftE<L, R> implements Either<L, R> {

        L lft;

        private LeftE(L lft$) {
            lft = lft$;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L left() {
            return lft;
        }

        @Override
        public String stringPrefix() {
            return "Left: ";
        }


        @Override
        public R right() {
            throw new UnsupportedOperationException("right() is not supported for leftEither"); //To change body of generated methods, choose Tools | Templates.
        }
    }

    final class RightE<L, R> implements Either<L, R> {
        R rgt;

        private RightE(R rgt$) {
            rgt = rgt$;
        }


        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }


        @Override
        public String stringPrefix() {
            return "Right: ";
        }

        @Override
        public L left() {
            throw new UnsupportedOperationException("left() is not supported for rightEither"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public R right() {
            return rgt;
        }
    }
}

