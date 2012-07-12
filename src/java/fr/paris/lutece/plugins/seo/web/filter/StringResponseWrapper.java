/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.seo.web.filter;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 */
public class StringResponseWrapper extends HttpServletResponseWrapper
{

    private StringWriter stringWriter;

    /**
     * Initializes wrapper. <P> First, this constructor calls the parent
     * constructor. That call is crucial so that the response is stored and thus
     * setHeader, setStatus, addCookie, and so forth work normally. <P> Second,
     * this constructor creates a StringWriter that will be used to accumulate
     * the response.
     */
    public StringResponseWrapper(HttpServletResponse response)
    {
        super(response);
        stringWriter = new StringWriter();
    }

    /**
     * When servlets or JSP pages ask for the Writer, don't give them the real
     * one. Instead, give them a version that writes into the StringBuffer. The
     * filter needs to send the contents of the buffer to the client (usually
     * after modifying it).
     */
    @Override
    public PrintWriter getWriter()
    {
        return (new PrintWriter(stringWriter));
    }

    /**
     * Similarly, when resources call getOutputStream, give them a phony output
     * stream that just buffers up the output.
     */
    @Override
    public ServletOutputStream getOutputStream()
    {
        return (new StringOutputStream(stringWriter));
    }

    /**
     * Get a String representation of the entire buffer. <P> Be sure <I>not</I>
     * to call this method multiple times on the same wrapper. The API for
     * StringWriter does not guarantee that it "remembers" the previous value,
     * so the call is likely to make a new String every time.
     */
    @Override
    public String toString()
    {
        return (stringWriter.toString());
    }

    /**
     * Get the underlying StringBuffer.
     */
    public StringBuffer getBuffer()
    {
        return (stringWriter.getBuffer());
    }
}
