/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.validator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.xwiki.validator.DutchWebGuidelinesValidator;
import org.xwiki.validator.ValidationError.Type;

import junit.framework.TestCase;

public class DutchWebGuidelinesValidatorTest extends TestCase
{
    private DutchWebGuidelinesValidator validator;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        validator = new DutchWebGuidelinesValidator();
    }

    private void setValidatorDocument(InputStream document) throws Exception
    {
        validator.setDocument(document);
    }

    private void setValidatorDocument(String content) throws Exception
    {
        validator.setDocument(new ByteArrayInputStream(content.getBytes("UTF-8")));
    }

    private String getErrors(DutchWebGuidelinesValidator validator)
    {
        return validator.getErrors().toString();
    }

    private boolean isValid(DutchWebGuidelinesValidator validator)
    {
        boolean isValid = true;
        
        for (ValidationError error : validator.getErrors()) {
            if (error.getType() != Type.WARNING) {
                isValid = false;
            }
        }
        
        return isValid;
    }

    // All

    public void testValidate() throws Exception
    {
        setValidatorDocument(getClass().getResourceAsStream("/dwg-valid.html"));
        validator.validate();
        
        for (ValidationError error : validator.getErrors()) {            
            System.err.println(error);
        }
        
        assertTrue(getErrors(validator), isValid(validator));
    }

    // RPD 1s3

    public void testRpd1s3LinkValid() throws Exception
    {
        setValidatorDocument("<a href='test'>test</a>");
        validator.validateRpd1s3();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3LinkJavascript() throws Exception
    {
        setValidatorDocument("<a href='javascript:'>test</a>");
        validator.validateRpd1s3();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3LinkWrongAttribute() throws Exception
    {
        setValidatorDocument("<a href='test' mouseover=''>test</a>");
        validator.validateRpd1s3();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3FormValidSubmit() throws Exception
    {
        setValidatorDocument("<form><fieldset><submit/></fieldset></form>");
        validator.validateRpd1s3();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3FormValidInput() throws Exception
    {
        setValidatorDocument("<form><fieldset><input type='submit' /></fieldset></form>");
        validator.validateRpd1s3();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3FormInvalidImageInput() throws Exception
    {
        setValidatorDocument("<form><fieldset><input type='image' alt='' /></fieldset></form>");
        validator.validateRpd1s3();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3FormValidImageInput() throws Exception
    {
        setValidatorDocument("<form><fieldset><input type='image' alt='submit' /></fieldset></form>");
        validator.validateRpd1s3();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd1s3FormNoSubmit() throws Exception
    {
        setValidatorDocument("<form></form>");
        validator.validateRpd1s3();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 2s3

    public void testRpd2s3NoDoctype() throws Exception
    {
        setValidatorDocument("<html></html>");
        validator.validateRpd2s3();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 2s4

    public void testRpd2s4NoDoctype() throws Exception
    {
        setValidatorDocument("<html></html>");
        validator.validateRpd2s4();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 2s5

    public void testRpd2s5ValidDoctype() throws Exception
    {
        setValidatorDocument("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' "
            + "'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'><html></html>");
        validator.validateRpd2s5();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd2s5FramesetDoctype() throws Exception
    {
        setValidatorDocument("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Frameset//EN' "
            + "'http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd'><html></html>");
        validator.validateRpd2s5();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd2s5FramesetTag() throws Exception
    {
        setValidatorDocument("<frameset></frameset>");
        validator.validateRpd2s5();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd2s5FrameTag() throws Exception
    {
        setValidatorDocument("<frame></frame>");
        validator.validateRpd2s5();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s1

    public void testRpd3s1BoldMarkup() throws Exception
    {
        setValidatorDocument("<p><b></b></p>");
        validator.validateRpd3s1();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd3s1ItalicMarkup() throws Exception
    {
        setValidatorDocument("<p><i></i></p>");
        validator.validateRpd3s1();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s2

    public void testRpd3s2NoHeading() throws Exception
    {
        setValidatorDocument("<body></body>");
        validator.validateRpd3s2();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s3

    public void testRpd3s3HeadingsValid() throws Exception
    {
        setValidatorDocument("<body><h1></h1><h2></h2><h2></h2><h3></h3></body>");
        validator.validateRpd3s3();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd3s3HeadingsMissingLevel() throws Exception
    {
        setValidatorDocument("<body><h1></h1><h3></h3></body>");
        validator.validateRpd3s3();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s4

    public void testRpd3s4ValidParagraphs() throws Exception
    {
        setValidatorDocument("<body><p>content<br/>content<br/>content<br/></p>"
            + "<p>content<br/>content<br/>content<br/></p><p>content<br/>content<br/>content<br/></p></body>");
        validator.validateRpd3s4();
        assertTrue(getErrors(validator), isValid(validator));
    }

    public void testRpd3s4MissingParagraph() throws Exception
    {
        // Consecutive line breaks.
        setValidatorDocument("<body><p>content<br/><br/>content</p></body>");
        validator.validateRpd3s4();
        assertFalse(getErrors(validator), isValid(validator));

        // Consecutive line breaks separated by white spaces.
        setValidatorDocument("<body><p>content<br/>   <br/>content</p></body>");
        validator.validateRpd3s4();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s5

    public void testRpd3s5InvalidMarkup() throws Exception
    {
        setValidatorDocument("<body><p><b>bold</b></p></body>");
        validator.validateRpd3s5();
        assertFalse(getErrors(validator), isValid(validator));

        setValidatorDocument("<body><p><i>italic</i></p></body>");
        validator.validateRpd3s5();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s9

    public void testRpd3s9Sub() throws Exception
    {
        setValidatorDocument("<body><p><sub>sub</sub></p></body>");
        validator.validateRpd3s9();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd3s9Sup() throws Exception
    {
        setValidatorDocument("<body><p><sup>sup</sup></p></body>");
        validator.validateRpd3s9();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s11

    public void testRpd3s11Quotation() throws Exception
    {
        setValidatorDocument("<body><p><q>quotation</q></p></body>");
        validator.validateRpd3s11();
        assertFalse(getErrors(validator), isValid(validator));
    }

    // RPD 3s13

    public void testRpd3s13BulletList() throws Exception
    {
        setValidatorDocument("<body> * item1<br/> * item2 <br/> * item3</body>");
        validator.validateRpd3s13();
        assertFalse(getErrors(validator), isValid(validator));

        setValidatorDocument("<body>*item1<br/>*item2<br/>*item3</body>");
        validator.validateRpd3s13();
        assertFalse(getErrors(validator), isValid(validator));

    }

    public void testRpd3s13DashList() throws Exception
    {
        setValidatorDocument("<body> - item1<br/> - item2 <br/> - item3</body>");
        validator.validateRpd3s13();
        assertFalse(getErrors(validator), isValid(validator));

        setValidatorDocument("<body>-item1<br/>-item2 <br/>-item3</body>");
        validator.validateRpd3s13();
        assertFalse(getErrors(validator), isValid(validator));
    }

    public void testRpd3s13NumberedList() throws Exception
    {
        setValidatorDocument("<body> 1. item1<br/> 2. item2 <br/> 3. item3</body>");
        validator.validateRpd3s13();
        assertFalse(getErrors(validator), isValid(validator));

        setValidatorDocument("<body>1.item1<br/>2.item2<br/>3.item3</body>");
        validator.validateRpd3s13();
        assertFalse(getErrors(validator), isValid(validator));
    }

}
