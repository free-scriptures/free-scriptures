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



directories = ./bommanager ./gui ./html2epub ./latex ./osis2haggai ./schemavalidator ./txtreplace ./vpl2haggai ./workflows ./xml_split ./xsltransformator



.PHONY: all $(directories) install



all: $(directories)



$(directories):
	$(MAKE) --directory=$@

install:
	java -classpath ./workflows/ setup1

