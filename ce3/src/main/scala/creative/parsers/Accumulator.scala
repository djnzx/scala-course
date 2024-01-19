/*
 * Copyright 2022 Creative Scala
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

package creative.parsers

trait Accumulator[Input, Output] {
  type Accum

  def create(): Accum
  def append(accum: Accum, in: Input): Accum
  def retrieve(accum: Accum): Output
}

object Accumulator {
  implicit val charStringBuilderStringInstance: Accumulator[Char, String] =
    new Accumulator[Char, String] {
      type Accum = StringBuilder

      def create(): StringBuilder = new StringBuilder()
      def append(accum: StringBuilder, in: Char): StringBuilder =
        accum.append(in)
      def retrieve(accum: StringBuilder): String = accum.result()
    }
}
