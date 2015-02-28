# Copyright (C) 2015  Stephan Kreutzer
#
# This file is part of osis2pdf1 workflow.
#
# osis2pdf1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# osis2pdf1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with osis2pdf1 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all osis2pdf1 clean



all: osis2pdf1
osis2pdf1: osis2pdf1.class



osis2pdf1.class: osis2pdf1.java
	javac osis2pdf1.java

clean:
	rm -f ./osis2pdf1.class

