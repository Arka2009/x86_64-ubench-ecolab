#!/bin/sh
#####################################################################
# BenchIT - Performance Measurement for Scientific Applications
# Contact: developer@benchit.org
#
# $Id: COMPILE.SH 1 2009-09-11 12:26:19Z william $
# $URL: svn+ssh://william@rupert.zih.tu-dresden.de/svn-base/benchit-root/BenchITv6/kernel/numerical/gauss/F95/0/0/double/COMPILE.SH $
# For license details see COPYING in the package base directory
#####################################################################
# Kernel: Gaussian Linear Equation System Solver
#####################################################################

# If it's not set, the user
# executed this script directly so we need to call top script
if [ -z "${BENCHIT_COMPILE_SH_IS_CALLED}" ]; then
   # Go to COMPILE.SH directory
   cd `dirname ${0}` || exit 1

   # compile using global script
   ../../../../../../../COMPILE.SH `pwd`
   exit 0
fi


LOCAL_KERNEL_COMPILER="F95"
LOCAL_KERNEL_COMPILERFLAGS=""
LOCAL_KERNEL_FILES="gauss.f90"

LOCAL_MODULE_FILES="kernel_main.c init_mat.c"

LOCAL_BENCHIT_COMPILER="C"
LOCAL_BENCHIT_COMPILERFLAGS=""

LOCAL_LINKERFLAGS=""

BENCHIT_COMMENT="Gaussian Linear Equation System Solver"
export BENCHIT_COMMENT