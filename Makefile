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
CC=icc
CC_ARGS=-static-intel -shared -fast -fp-model fast=2 -Wc
LIB_EXT=.dylib
JAVA_PLATFORM_INCLUDE=$(shell \
	if test -d $(JAVA_INCLUDE)/darwin; then \
		echo $(JAVA_INCLUDE)/darwin; \
	fi;)

ifeq ($(strip $(JAVA_PLATFORM_INCLUDE)),)
	CC_ARGS:=-fPIC $(CC_ARGS)
	LIB_EXT=.so
	JAVA_PLATFORM_INCLUDE=$(shell \
		if test -d $(JAVA_INCLUDE)/linux; then \
			echo $(JAVA_INCLUDE)/linux; \
		fi;)
endif

ifeq ($(strip $(JAVA_PLATFORM_INCLUDE)),)
	TMP_DIR=target/c
	CC=icl
	CC_ARGS=-LD -MT -Qstd=c99 -Qipp -Qipp-link:static -O3 -fp:fast=2 /Fa$(TMP_DIR)/ /Fo$(TMP_DIR)/ /Fi$(TMP_DIR)/ -DPTW32_STATIC_LIB -I"$(SRC_DIR)/win32" $(SRC_DIR)/win32/libpthreadVC3.lib
	LINK_ARGS=-link -implib:$(TMP_DIR)/libmath.lib
	LIB_EXT=.dll
	JAVA_PLATFORM_INCLUDE=$(shell \
		if test -d "$(JAVA_INCLUDE)/win32"; then \
			echo "$(JAVA_INCLUDE)/win32"; \
			mkdir -p "$(TMP_DIR)"; \
		fi;)
endif

# gcc: -shared -Ofast -fPIC
# -axCORE-AVX512,CORE-AVX2,AVX -xSSE4.2
# -axCORE-AVX2,AVX,SSE4.2
BUILD=$(CC) -o $(TARGET_LIB)$(1)$(LIB_EXT) $(2) \
	-I"$(JAVA_PLATFORM_INCLUDE)" \
	-I"$(JAVA_INCLUDE)" \
 	$(CC_ARGS) \
	$(shell find $(SRC_DIR) -name '*.c') $(LINK_ARGS)

default: $(TARGET_DIR)
	$(call BUILD,j)
	$(call BUILD,c,-DCRITICAL_NATIVE)

$(TARGET_DIR):
	mkdir -p $(TARGET_DIR)

# Delete Compiled Object and Binary Files
clean:
	@rm -rf $(TARGET_DIR)

# Fake targets
.PHONY: default clean