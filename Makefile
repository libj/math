# Copyright (c) 2020 Seva Safris, LibJ
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# You should have received a copy of The MIT License (MIT) along with this
# program. If not, see <http://opensource.org/licenses/MIT/>.

# C Library Name
C_TARGET_LIB_NAME=math

# Directories
C_SRC_DIR=src/main/c
BLD_DIR=target

# Java Headers Directories
JAVA_INCLUDE=$(shell echo $$JAVA_HOME)/include

# Java Platform Dependant Header Directories
JAVA_PLATFORM_INCLUDE=$(shell \
	if test -d $(JAVA_INCLUDE)/linux;\
		then echo $(JAVA_INCLUDE)/linux; \
	fi;)

ifeq ($(strip $(JAVA_PLATFORM_INCLUDE)),)
	JAVA_PLATFORM_INCLUDE=$(shell \
		if test -d $(JAVA_INCLUDE)/darwin;\
			then echo $(JAVA_INCLUDE)/darwin; \
		fi;)
endif

ifeq ($(strip $(JAVA_PLATFORM_INCLUDE)),)
	JAVA_PLATFORM_INCLUDE=$(shell \
		if test -d $(JAVA_INCLUDE)/win32;\
			then echo $(JAVA_INCLUDE)/win32; \
		fi;)
endif

# C Compiler
CC=gcc

# C_LIB
C_TARGET_LIB_FILENAME=lib$(C_TARGET_LIB_NAME).so
C_TARGET_LIB=$(BLD_DIR)/$(C_TARGET_LIB_FILENAME)

# Default Target
default: $(C_TARGET_LIB)

# C Library
$(C_TARGET_LIB):
	@mkdir -p $(BLD_DIR)
	@echo Creating C library...
	@$(CC) \
		-I$(JAVA_PLATFORM_INCLUDE) \
		-I$(JAVA_INCLUDE) \
		-shared \
		-Ofast \
		-fPIC \
		-o $(C_TARGET_LIB) \
		$(shell find $(C_SRC_DIR) -name '*.c')

# Delete Compiled Object and Binary Files
clean:
	@echo Deleting class files...
	@rm -rf $(BLD_DIR)

# Fake targets
.PHONY: default clean