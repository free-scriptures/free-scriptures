<?xml version="1.0" encoding="UTF-8"?>
<!--
Copyright (C) 2012-2015  Stephan Kreutzer

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
    <xsl:text>\documentclass{article}&#xA;</xsl:text>
    <xsl:text>% This file was generated by haggai2latex8.xsl, which is free software licensed under the GNU Affero General Public License 3 or any later version (see https://github.com/free-scriptures/free-scriptures/ and http://www.free-scriptures.org).&#xA;</xsl:text>
    <xsl:text>&#xA;</xsl:text>
    <xsl:text>\usepackage[utf8]{inputenc}&#xA;</xsl:text>
    <xsl:text>\usepackage[a5paper,left=1.5cm,right=1.5cm,top=1cm,bottom=0.25cm,includeheadfoot]{geometry}&#xA;</xsl:text>
    <xsl:text>\usepackage{ngerman}&#xA;</xsl:text>
    <xsl:text>\usepackage{multicol}&#xA;</xsl:text>
    <xsl:text>\usepackage{lettrine}&#xA;</xsl:text>
    <xsl:text>\usepackage{bigfoot}&#xA;</xsl:text>
    <xsl:text>\usepackage{perpage}&#xA;</xsl:text>
    <xsl:text>\usepackage{zref-savepos}&#xA;</xsl:text>
    <xsl:text>\usepackage{fancyhdr}&#xA;</xsl:text>
    <xsl:text>&#xA;</xsl:text>
    <xsl:text>\setlength{\parskip}{0pt}&#xA;</xsl:text>
    <!-- multicol -->
    <xsl:text>\setlength{\columnsep}{0.5cm}&#xA;</xsl:text>
    <xsl:text>\setlength{\columnseprule}{0.5pt}&#xA;</xsl:text>
    <!-- bigfoot -->
    <xsl:text>\DeclareNewFootnote[para]{default}&#xA;</xsl:text>
    <xsl:text>\expandafter\def\csname @makefnbreak\endcsname{\unskip\linebreak[0]\quad}&#xA;</xsl:text>
    <!-- perpage -->
    <xsl:text>\MakePerPage{footnotedefault}&#xA;</xsl:text>
    <!-- zref-savepos -->
    <xsl:text>
\def\putmarginpar#1#2%
{%
  \zsavepos{#1}%
  % For the magical number: see the .aux file for x-positions of the inserted labels.
  % The number there is of the \textasteriskcentered as first character of a line on
  % the right column.
  \ifnum14592481>\number\zposx{#1}%
    \ifnum#2&lt;10%
      \hbox to 0pt{\hskip\dimexpr-\zposx{#1}sp +1.08cm \relax#2}%
    \else%
      \ifnum#2&gt;99%
        \hbox to 0pt{\hskip\dimexpr-\zposx{#1}sp +0.73cm \relax#2}%
      \else
        \hbox to 0pt{\hskip\dimexpr-\zposx{#1}sp +0.89cm \relax#2}%
      \fi
    \fi
  \else%
    \hbox to 0pt{\hskip\dimexpr-\zposx{#1}sp +13.5cm \relax#2}%
  \fi%
}%
&#xA;</xsl:text>
    <!-- fancyhdr -->
    <xsl:text>\pagestyle{fancy}&#xA;</xsl:text>
    <xsl:text>\fancyhead{}&#xA;</xsl:text>
    <xsl:text>\fancyhead[C]{\leftmark}&#xA;</xsl:text>
    <xsl:text>\setlength{\headsep}{4.6pt}&#xA;</xsl:text>
    <xsl:text>\renewcommand{\headrulewidth}{0.5pt}&#xA;</xsl:text>
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
    <xsl:text>\large</xsl:text>
    <xsl:choose>
      <xsl:when test="./CAPTION">
        <xsl:text> </xsl:text><xsl:value-of select="./CAPTION"/><xsl:text>&#xA;</xsl:text>
      </xsl:when>
      <xsl:when test="./@bname">
        <xsl:text> </xsl:text><xsl:value-of select="./@bname"/><xsl:text>&#xA;</xsl:text>
      </xsl:when>
      <xsl:when test="./@bsname">
        <xsl:text> </xsl:text><xsl:value-of select="./@bsname"/><xsl:text>&#xA;</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>\MakeUppercase{\romannumeral </xsl:text><xsl:value-of select="./@bnumber"/><xsl:text>}&#xA;</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>\end{center}&#xA;</xsl:text>
    <xsl:text>\begin{multicols}{2}&#xA;</xsl:text>
    <xsl:apply-templates select="CHAPTER"/>
    <xsl:text>\end{multicols}&#xA;</xsl:text>
    <xsl:text>\pagebreak{}&#xA;</xsl:text>
  </xsl:template>
  <xsl:template match="CHAPTER">
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
    <xsl:text> </xsl:text><xsl:value-of select="./@cnumber"/><xsl:text>}</xsl:text>
    <xsl:text>\lettrine[findent=5pt,nindent=0pt]{</xsl:text>
    <xsl:value-of select="./@cnumber"/>
    <xsl:text>}{}</xsl:text>
    <xsl:apply-templates select="VERSE | PARAGRAPH"/>
  </xsl:template>
  <xsl:template match="PARAGRAPH">
    <xsl:apply-templates select="VERSE"/>
    <!-- Default TeX paragraph. -->
    <xsl:text>&#xA;</xsl:text>
  </xsl:template>
  <xsl:template match="VERSE">
    <xsl:if test="not(@vnumber = 1)">
      <xsl:text>\textasteriskcentered</xsl:text>
    </xsl:if>
    <!-- xsl:choose>
      <xsl:when test="local-name(parent::*)='PARAGRAPH'">
        <xsl:choose>
          <xsl:when test="position()=1">
            What put here to get \zsavepos working?
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>\textasteriskcentered</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>\textasteriskcentered</xsl:text>
      </xsl:otherwise>
    </xsl:choose -->
    <xsl:text>\putmarginpar{versepos:</xsl:text>
    <xsl:choose>
      <xsl:when test="local-name(parent::*)='PARAGRAPH'">
        <xsl:value-of select="../../../@bnumber"/><xsl:text>:</xsl:text><xsl:value-of select="../../@cnumber"/><xsl:text>:</xsl:text><xsl:value-of select="@vnumber"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="../../@bnumber"/><xsl:text>:</xsl:text><xsl:value-of select="../@cnumber"/><xsl:text>:</xsl:text><xsl:value-of select="@vnumber"/>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text>}{</xsl:text>
    <xsl:value-of select="@vnumber"/>
    <xsl:text>}</xsl:text>
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