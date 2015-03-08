# Copyright (C) 2015  Stephan Kreutzer
#
# This file is part of haggai2pdf2 workflow.
#
# haggai2pdf2 workflow is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License version 3 or any later version,
# as published by the Free Software Foundation.
#
# haggai2pdf2 workflow is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU Affero General Public License 3 for more details.
#
# You should have received a copy of the GNU Affero General Public License 3
# along with haggai2pdf2 workflow. If not, see <http://www.gnu.org/licenses/>.



.PHONY: all haggai2pdf2 clean



all: haggai2pdf2
haggai2pdf2: haggai2pdf2.class



haggai2pdf2.class: haggai2pdf2.java
	javac haggai2pdf2.java

clean:
	rm -f ./haggai2pdf2.class

