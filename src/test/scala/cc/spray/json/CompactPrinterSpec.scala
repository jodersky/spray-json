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

package cc.spray.json

import org.specs2.mutable._

class CompactPrinterSpec extends Specification {

  "The CompactPrinter" should {
    "print JsNull to 'null'" in {
      CompactPrinter(JsNull) mustEqual "null"
    }
    "print JsTrue to 'true'" in {
      CompactPrinter(JsTrue) mustEqual "true"
    }
    "print JsFalse to 'false'" in {
      CompactPrinter(JsFalse) mustEqual "false"
    }
    "print JsNumber(0) to '0'" in {
      CompactPrinter(JsNumber(0)) mustEqual "0"
    }
    "print JsNumber(1.23) to '1.23'" in {
      CompactPrinter(JsNumber(1.23)) mustEqual "1.23"
    }
    "print JsNumber(1.23) to '1.23'" in {
      CompactPrinter(JsNumber(1.23)) mustEqual "1.23"
    }
    "print JsNumber(-1E10) to '-1E10'" in {
      CompactPrinter(JsNumber(-1E10)) mustEqual "-1.0E+10"
    }
    "print JsNumber(12.34e-10) to '12.34e-10'" in {
      CompactPrinter(JsNumber(12.34e-10)) mustEqual "1.234E-9"
    }
    "print JsString(\"xyz\") to \"xyz\"" in {
      CompactPrinter(JsString("xyz")) mustEqual "\"xyz\""
    }
    "properly escape special chars in JsString" in {
      CompactPrinter(JsString("\"\\\b\f\n\r\t\u12AB")) mustEqual """"\"\\\b\f\n\r\t""" + "\\u12ab\""
    }
    "properly print a simple JsObject" in (
      CompactPrinter(JsObject("key" -> JsNumber(42), "key2" -> JsString("value")))
              mustEqual """{"key":42,"key2":"value"}"""
    )
    "properly print a simple JsArray" in (
      CompactPrinter(JsArray(JsNull, JsNumber(1.23), JsObject("key" -> JsBoolean(true))))
              mustEqual """[null,1.23,{"key":true}]"""
    )
    "properly print a JSON padding (JSONP) if requested" in {
      CompactPrinter(JsTrue, Some("customCallback")) mustEqual("customCallback(true)")
    }
  }
  
}