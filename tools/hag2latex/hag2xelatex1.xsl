<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2012-2014  Stephan Kreutzer

This file is part of Free Scriptures.

Free Scriptures is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3 or any later version,
as published by the Free Software Foundation.

Free Scriptures is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License 3 for more details.

You should have received a copy of the GNU Affero General Public License 3
along with Free Scriptures. If not, see <http://www.gnu.org/licenses/>.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" encoding="UTF-8"/>

  <xsl:template match="XMLBIBLE">
    <xsl:text>\documentclass[a4paper,twoside]{article}&#xA;</xsl:text>
    <xsl:text>% This file was created by hag2xelatex1.xsl (see http://www.free-scriptures.org), which is free software licensed under the GNU Affero General Public License 3 or any later version.&#xA;</xsl:text>
    <xsl:text>&#xA;</xsl:text>
    <xsl:text>\usepackage{xunicode}&#xA;</xsl:text>
    <xsl:text>\usepackage{xltxtra}&#xA;</xsl:text>
    <xsl:text>\usepackage{ngerman}&#xA;</xsl:text>
    <xsl:text>\usepackage{fancyhdr}&#xA;</xsl:text>
    <xsl:text>&#xA;</xsl:text>
    <xsl:text>\setlength{\parskip}{0pt}&#xA;</xsl:text>
    <xsl:text>&#xA;</xsl:text>
    <!-- fancyhdr -->
    <xsl:text>\pagestyle{fancy}&#xA;</xsl:text>
    <xsl:text>\fancyhead{}&#xA;</xsl:text>
    <xsl:text>\fancyhead[LE]{\leftmark}&#xA;</xsl:text>
    <xsl:text>\fancyhead[RO]{\rightmark}&#xA;</xsl:text>
    <xsl:text>\setlength{\headsep}{4.6pt}&#xA;</xsl:text>
    <xsl:text>
\makeatletter
  \renewcommand\footnoterule{%
  \kern-3\p@
  \hrule\@width \textwidth
  \kern2.6\p@}
\makeatother
&#xA;</xsl:text>
    <xsl:text>\begin{document}&#xA;</xsl:text>
    <xsl:apply-templates select="BIBLEBOOK"/>
    <xsl:text>\end{document}&#xA;</xsl:text>
  </xsl:template>

  <xsl:template match="BIBLEBOOK">
    <xsl:text>\begin{center}&#xA;</xsl:text>
    <xsl:text>\LARGE \textbf{</xsl:text>
    <xsl:choose>
      <xsl:when test="./CAPTION">
        <xsl:value-of select="./CAPTION"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="./@bnumber"/>
        <xsl:text>. Buch</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>}&#xA;</xsl:text>
    <xsl:text>\end{center}&#xA;</xsl:text>
    <xsl:text>\vspace*{\baselineskip}&#xA;</xsl:text>
    <xsl:apply-templates select="CHAPTER"/>
    <xsl:text>\pagebreak{}&#xA;</xsl:text>
  </xsl:template>
  <xsl:template match="CHAPTER">
    <xsl:text>\begin{center}&#xA;</xsl:text>
    <xsl:text>\textbf{\large Kapitel </xsl:text>
    <xsl:value-of select="./@cnumber"/>
    <xsl:text>}&#xA;</xsl:text>
    <xsl:text>\end{center}&#xA;</xsl:text>
    <xsl:text>\markboth{</xsl:text>
    <xsl:choose>
      <xsl:when test="../@bname">
        <xsl:value-of select="../@bname"/>
      </xsl:when>
      <xsl:when test="./@bsname">
        <xsl:value-of select="../@bsname"/>
      </xsl:when>
    </xsl:choose>
    <xsl:text> </xsl:text><xsl:value-of select="./@cnumber"/><xsl:text>}{</xsl:text>
    <xsl:choose>
      <xsl:when test="../@bname">
        <xsl:value-of select="../@bname"/>
      </xsl:when>
      <xsl:when test="../@bsname">
        <xsl:value-of select="../@bsname"/>
      </xsl:when>
    </xsl:choose>
    <xsl:text> </xsl:text><xsl:value-of select="./@cnumber"/><xsl:text>}&#xA;</xsl:text>
    <xsl:apply-templates select="VERSE | PARAGRAPH"/>
    <xsl:text>\vspace*{\baselineskip}&#xA;</xsl:text>
  </xsl:template>
  <xsl:template match="PARAGRAPH">
    <xsl:apply-templates select="VERSE"/>
    <!-- Default TeX paragraph. -->
    <xsl:text>&#xA;</xsl:text>
  </xsl:template>
  <xsl:template match="VERSE">
    <xsl:apply-templates/>
    <xsl:text>&#xA;</xsl:text>
  </xsl:template>

  <xsl:template match="VERSE//text()">
    <xsl:value-of select="."/>
  </xsl:template>
  <xsl:template match="NOTE">
    <xsl:text>\footnote{</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>}</xsl:text>
  </xsl:template>
  <xsl:template match="STYLE">
    <xsl:choose>
      <xsl:when test="@fs='super'">
        <xsl:text>$\langle$</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>$\rangle$</xsl:text>
      </xsl:when>
      <xsl:when test="@fs='emphasis'">
        <xsl:text>\textbf{</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>}</xsl:text>
      </xsl:when>
      <xsl:when test="@fs='italic'">
        <xsl:text>\textit{</xsl:text>
        <xsl:value-of select="."/>
        <xsl:text>}</xsl:text>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="NOTE//text()">
    <xsl:value-of select="."/>
  </xsl:template>

</xsl:stylesheet>
