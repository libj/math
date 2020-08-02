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
TARGET_LIB_NAME=math

# Directories
SRC_DIR=src/main/c
TARGET_DIR=src/main/resources

TARGET_LIB=$(TARGET_DIR)/lib$(TARGET_LIB_NAME)

# Java Headers Directories
JAVA_INCLUDE=$(shell echo $$JAVA_HOME)/include

# Java Platform Dependant Header Directories
CC_ARGS=-fPIC
LIB_EXT=.so
JAVA_PLATFORM_INCLUDE=$(shell \
	if test -d $(JAVA_INCLUDE)/linux; \
		then echo $(JAVA_INCLUDE)/linux; \
	fi;)

ifeq ($(strip $(JAVA_PLATFORM_INCLUDE)),)
	CC_ARGS=
	LIB_EXT=.dylib
	JAVA_PLATFORM_INCLUDE=$(shell \
		if test -d $(JAVA_INCLUDE)/darwin; \
			then echo $(JAVA_INCLUDE)/darwin; \
		fi;)
endif

ifeq ($(strip $(JAVA_PLATFORM_INCLUDE)),)
	CC_ARGS=
	LIB_EXT=.dll
	JAVA_PLATFORM_INCLUDE=$(shell \
		if test -d $(JAVA_INCLUDE)/win32; \
			then echo $(JAVA_INCLUDE)/win32; \
		fi;)
endif

# C Compiler
CC=icpc

# gcc: -shared -Ofast -fPIC
# -axCORE-AVX512,CORE-AVX2,AVX -xSSE4.2
# -axCORE-AVX2,AVX,SSE4.2
BUILD=$(CC) -o $(TARGET_LIB)$(1)$(LIB_EXT) $(2) $(CC_ARGS) \
	-I$(JAVA_PLATFORM_INCLUDE) \
	-I$(JAVA_INCLUDE) \
	-static-intel \
	-shared \
	-fast -fp-model fast=2 \
	$(shell find $(SRC_DIR) -name '*.c')

default: $(TARGET_DIR)
	$(call BUILD,j)
	$(call BUILD,c,-Dcritical)

$(TARGET_DIR):
	mkdir -p $(TARGET_DIR)

# Delete Compiled Object and Binary Files
clean:
	@rm -rf $(TARGET_DIR)

# Fake targets
.PHONY: default clean