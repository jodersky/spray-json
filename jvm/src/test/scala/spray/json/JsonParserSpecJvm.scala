/*
 * Copyright (C) 2011 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spray.json

import org.specs2.mutable._

class JsonParserSpecJvm extends Specification {

  "The JsonParser (on the JVM)" should {
    "be reentrant" in {
      val largeJsonSource = scala.io.Source.fromInputStream(getClass.getResourceAsStream("/test.json")).mkString
      import scala.collection.parallel.immutable.ParSeq
      ParSeq.fill(20)(largeJsonSource).map(JsonParser(_)).toList.map {
        _.asInstanceOf[JsObject].fields("questions").asInstanceOf[JsArray].elements.size
      } === List.fill(20)(100)
    }
  }

}
