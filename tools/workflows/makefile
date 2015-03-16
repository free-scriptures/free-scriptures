# Copyright (C) 2015  Stephan Kreutzer
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



directories = ./haggai2epub1 ./haggai2html1 ./haggai2pdf1 ./haggai2pdf2 ./osis2epub1 ./osis2haggai1 ./osis2html1 ./osis2pdf1



.PHONY: all workflows $(directories) clean



all: workflows $(directories)
workflows: setup1.class



setup1.class: setup1.java
	javac setup1.java

$(directories):
	$(MAKE) --directory=$@

clean:
	rm -f ./setup1.class	

