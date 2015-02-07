<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2012-2013  Stephan Kreutzer

This file is part of Freie Bibel.

Freie Bibel is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3 or any later version,
as published by the Free Software Foundation.

Freie Bibel is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License 3 for more details.

You should have received a copy of the GNU General Public License
along with Freie Bibel. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"/>
  <xsl:template match="XMLBIBLE">
    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
      <head>
        <title><xsl:value-of select="/XMLBIBLE/@biblename"/></title>
        <meta name="description" content="{/XMLBIBLE/INFORMATION/description}"/>
        <meta name="copyright" content="{/XMLBIBLE/INFORMATION/rights}"/>
        <meta name="author" content="{/XMLBIBLE/INFORMATION/publisher}"/>
        <meta name="generator" content="hag2html.xsl (http://www.freie-bibel.de)"/>
        <meta name="keywords" content="{/XMLBIBLE/@biblename}"/>
        <meta http-equiv="expires" content="1296000"/>
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
      </head>
      <body>
        <h1><xsl:value-of select="/XMLBIBLE/@biblename"/></h1>
        <table border="1">
          <tr>
            <th>Meta</th>
            <th>Wert</th>
          </tr>
          <xsl:for-each select="INFORMATION/*">
            <tr>
              <td><xsl:value-of select="local-name()"/></td>
              <td><pre><xsl:value-of select="."/></pre></td>
            </tr>
          </xsl:for-each>
        </table>
        <xsl:apply-templates select="BIBLEBOOK"/>
      </body>
    </html>
  </xsl:template>
  <xsl:template match="BIBLEBOOK">
    <xsl:apply-templates select="CHAPTER"/>
  </xsl:template>
  <xsl:template match="CHAPTER">
    <div>
      <h2><xsl:value-of select="../@bname"/><xsl:text>&#x20;</xsl:text><xsl:value-of select="@cnumber"/></h2>
      <div>
        <xsl:apply-templates select="VERSE | PARAGRAPH"/>
      </div>
    </div>
  </xsl:template>
  <xsl:template match="PARAGRAPH">
    <p>
      <xsl:apply-templates select="VERSE"/>
    </p>
  </xsl:template>
  <xsl:template match="VERSE">
    <xsl:choose>
      <xsl:when test="local-name(parent::*)='PARAGRAPH'">
        <a name="verse_{../../../@bnumber}_{../../@cnumber}_{@vnumber}" id="verse_{../../../@bnumber}_{../../@cnumber}_{@vnumber}"/>
      </xsl:when>
      <xsl:otherwise>
        <a name="verse_{../../@bnumber}_{../@cnumber}_{@vnumber}" id="verse_{../../@bnumber}_{../@cnumber}_{@vnumber}"/>
      </xsl:otherwise>
    </xsl:choose>
    <sup>
      <xsl:value-of select="@vnumber"/>
    </sup>
    <xsl:apply-templates/>
    <xsl:text>&#x20;</xsl:text>
  </xsl:template>
  <xsl:template match="VERSE//text()">
    <xsl:value-of select="."/>
  </xsl:template>
  <xsl:template match="STYLE">
    <xsl:choose>
      <xsl:when test="@fs='super'">
        &#x27E8;<xsl:value-of select="."/>&#x27E9;
      </xsl:when>
      <xsl:when test="@fs='emphasis'">
        <b><xsl:value-of select="."/></b>
      </xsl:when>
      <xsl:when test="@fs='italic'">
        <i><xsl:value-of select="."/></i>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="NOTE">
    <abbr title="{.}" lang="de">*</abbr>
  </xsl:template>
</xsl:stylesheet>
