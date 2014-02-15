# Copyright (C) 2014  Stephan Kreutzer
#
# This file is part of Free Scriptures.
#
# Free Scriptures is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# Free Scriptures is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with Free Scriptures. If not, see <http://www.gnu.org/licenses/>.



PREFIX ?= /usr/local
BINDIR ?= ${PREFIX}/bin
.PHONY: osis2hag1 install clean



osis2hag1: osis2hag1.class

ConfigProcessor.class: ConfigProcessor.java
	javac ConfigProcessor.java

OSISProcessor.class: OSISProcessor.java
	javac OSISProcessor.java

osis2hag1.class: osis2hag1.java ConfigProcessor.class OSISProcessor.class
	javac osis2hag1.java

${BINDIR}:
	mkdir -p ${BINDIR}

install: osis2hag1 ${BINDIR}
	mkdir -p ${BINDIR}/html2epub1
	cp ConfigProcessor.class ${BINDIR}/osis2hag1
	cp OSISProcessor.class ${BINDIR}/osis2hag1
	cp osis2hag1.class ${BINDIR}/osis2hag1

clean:
	rm -f osis2hag1.class
	rm -f OSISProcessor.class
	rm -f ConfigProcessor.class

