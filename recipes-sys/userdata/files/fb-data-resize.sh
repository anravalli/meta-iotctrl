#!/bin/bash
DISK="/dev/mmcblk0"
PART_NUM=3

echo "Extending partition ${DISK}p${PART_NUM} ..."

# 1. Use "parted" to extend the partion to the whale disk
parted -s $DISK resizepart $PART_NUM 100%

# 2. Notify the kernel for the change
partprobe $DISK

# 3. Extend also the filesystem with Resize2fs
resize2fs ${DISK}p${PART_NUM}

echo "...operation completed!"