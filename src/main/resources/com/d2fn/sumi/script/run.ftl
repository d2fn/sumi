#!/bin/sh

#
# template run script copied into each new sketch directory
#

sketch_args="$@"
echo "sketch_args => $sketch_args"
run_args='run ${sketchName}'
echo "run_args => $run_args $sketch_args"

BASEDIR=$(dirname $0)
$BASEDIR/gradlew -p $BASEDIR run -Pargs="$run_args $sketch_args"
