/* Derived from sql.flex by Gian Perrone for the BigMC project.
 *
 * Copyright 2011 Gian Perrone
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsyntaxpane.lexers;

import jsyntaxpane.Token;
import jsyntaxpane.TokenType;

%%

%public
%class BgmLexer
%extends DefaultJFlexLexer
%final
%unicode
%char
%type Token

%{
    /**
     * Default constructor is needed as we will always call the yyreset
     */
    public BgmLexer() {
        super();
    }

    @Override
    public int yychar() {
        return yychar;
    }

%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = "#" {InputCharacter}* {LineTerminator}?

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = 0 | [1-9][0-9]*

Reserved =
   "%inner"                 |
   "%outer"                 |
   "%name"                  |
   "%active"                |
   "%passive"               |
   "%property"              |
   "%check"                 
%%

<YYINITIAL> {

  {Reserved}                     { return token(TokenType.KEYWORD); }


  "("                            |
  ")"                            |
  "["                            |
  "]"                            |
  ";"                            |
  ":"                            |
  ","                            |
  "."                            |
  "->"                           |
  "||"                           |
  "&&"                           |
  "|"                            |
  "="                            |
  ">"                            |
  "<"                            |
  "!"                            |
  "-"                            |
  "forall"                       |
  "exists"                       |
  "if"                           |
  "then"                         |
  "else"                            { return token(TokenType.OPERATOR); }


  /* numeric literals */

  {DecIntegerLiteral}            { return token(TokenType.NUMBER); }

  /* comments */
  {Comment}                      { return token(TokenType.COMMENT); }

  /* whitespace */
  {WhiteSpace}+                  { /* skip */ }

  /* identifiers */
  {Identifier}                   { return token(TokenType.IDENTIFIER); }

  "$" {DecIntegerLiteral}	 { return token(TokenType.TYPE); }
}

/* error fallback */
.|\n                             {  }
<<EOF>>                          { return null; }

