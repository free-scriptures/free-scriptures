# Copyright (C) 2015  Stephan Kreutzer
#
# This file is part of haggai2pdf1 workflow.
#
# haggai2pdf1 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# haggai2pdf1 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with haggai2pdf1 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all haggai2pdf1 clean



all: haggai2pdf1
haggai2pdf1: haggai2pdf1.class



haggai2pdf1.class: haggai2pdf1.java
	javac haggai2pdf1.java

clean:
	rm -f ./haggai2pdf1.class

