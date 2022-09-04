<#--
// Licensed to the Apache Software Foundation (ASF) under one or more
// contributor license agreements.  See the NOTICE file distributed with
// this work for additional information regarding copyright ownership.
// The ASF licenses this file to you under the Apache License, Version 2.0
// (the "License"); you may not use this file except in compliance with
// the License.  You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
-->
SqlNode SqlLoad() :
{
    SqlParserPos pos;
    SqlIdentifier sourceType;
    String sourceObj;
    SqlIdentifier targetType;
    String targetObj;
    SqlParserPos mapPos;
    SqlNodeList colMapping;
    SqlColMapping colMap;
    String separator = "\t";
}
{
    <LOAD>
    {
        pos = getPos();
    }
    sourceType = CompoundIdentifier()
    <COLON>
    sourceObj = StringLiteralValue()
    <TO>
    targetType = CompoundIdentifier()
    <COLON>
    targetObj = StringLiteralValue()
    {
        mapPos = getPos();
    }
    <LPAREN>
    {
        colMapping = new SqlNodeList(mapPos);
        colMapping.add(readOneColMapping());
    }
    (
        <COMMA>
        {
            colMapping.add(readOneColMapping());
        }
    )*
    <RPAREN>
    [<SEPARATOR> separator=StringLiteralValue()]
    {
        return new SqlLoad(pos, new SqlLoadSource(sourceType, sourceObj),
                new SqlLoadSource(targetType, targetObj), colMapping, separator);
    }
}


JAVACODE String StringLiteralValue() {
    SqlNode sqlNode = StringLiteral();
    return ((NlsString) SqlLiteral.value(sqlNode)).getValue();
}

SqlNode readOneColMapping():
{
    SqlIdentifier fromCol;
    SqlIdentifier toCol;
    SqlParserPos pos;
}
{
    { pos = getPos();}
    fromCol = SimpleIdentifier()
    toCol = SimpleIdentifier()
    {
        return new SqlColMapping(pos, fromCol, toCol);
    }
}
