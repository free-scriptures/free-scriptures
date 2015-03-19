# Copyright (C) 2014-2015  Stephan Kreutzer
#
# This file is part of osis2haggai1.
#
# osis2haggai1 is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# osis2haggai1 is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with osis2haggai1. If not, see <http://www.gnu.org/licenses/>.



.PHONY: osis2haggai1 clean



osis2haggai1: osis2haggai1.class

ConfigProcessor.class: ConfigProcessor.java
	javac ConfigProcessor.java

OSISProcessor.class: OSISProcessor.java
	javac OSISProcessor.java

osis2haggai1.class: osis2haggai1.java ConfigProcessor.class OSISProcessor.class
	javac osis2haggai1.java

clean:
	rm -f osis2haggai1.class
	rm -f OSISProcessor.class
	rm -f ConfigProcessor.class
	rm -f StructureStackElement.class

