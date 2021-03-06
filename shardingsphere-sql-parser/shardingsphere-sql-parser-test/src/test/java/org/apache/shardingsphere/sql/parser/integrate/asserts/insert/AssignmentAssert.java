/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.integrate.asserts.insert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sql.parser.integrate.asserts.SQLStatementAssertMessage;
import org.apache.shardingsphere.sql.parser.integrate.jaxb.impl.insert.ExpectedAssignment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.ExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.complex.ComplexExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.simple.LiteralExpressionSegment;
import org.apache.shardingsphere.sql.parser.sql.segment.dml.expr.simple.ParameterMarkerExpressionSegment;
import org.apache.shardingsphere.test.sql.SQLCaseType;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssignmentAssert {
    
    /**
     * Assert actual expression segment is correct with expected assignment.
     *
     * @param assertMessage assert message
     * @param actual actual assignment
     * @param expected expected assignment
     * @param sqlCaseType SQL case type
     */
    public static void assertIs(final SQLStatementAssertMessage assertMessage, final ExpressionSegment actual, final ExpectedAssignment expected, final SQLCaseType sqlCaseType) {
        if (SQLCaseType.Placeholder == sqlCaseType) {
            if (null == expected.getTypeForPlaceholder()) {
                return;
            }
            assertThat(assertMessage.getText("SQL expression type for placeholder error: "), actual.getClass().getSimpleName(), is(expected.getTypeForPlaceholder()));
            assertThat(assertMessage.getText("SQL expression text for placeholder error: "), getText(actual), is(expected.getTextForPlaceholder()));
        } else {
            assertThat(assertMessage.getText("SQL expression type for literal error: "), actual.getClass().getSimpleName(), is(expected.getTypeForLiteral()));
            assertThat(assertMessage.getText("SQL expression text for literal error: "), getText(actual), is(expected.getTextForLiteral()));
        }
    }
    
    private static String getText(final ExpressionSegment expressionSegment) {
        if (expressionSegment instanceof ParameterMarkerExpressionSegment) {
            return "" + ((ParameterMarkerExpressionSegment) expressionSegment).getParameterMarkerIndex();
        }
        if (expressionSegment instanceof LiteralExpressionSegment) {
            return ((LiteralExpressionSegment) expressionSegment).getLiterals().toString();
        }
        if (expressionSegment instanceof ComplexExpressionSegment) {
            return ((ComplexExpressionSegment) expressionSegment).getText();
        }
        return "";
    }
}
