#!/bin/sh

BASEDIR=$(dirname $0)
$BASEDIR/gradlew -p $BASEDIR clean jar

# vars
build_dir="$BASEDIR/build"
src_dir="$BASEDIR/src/main"
script_dir="$src_dir/resources/com/d2fn/sumi/script"
template_dir="$src_dir/resources/com/d2fn/sumi/template"
sumi_run_script_name="sumi"
src_jar_dir="$build_dir/libs"
tmp_install_dir="$build_dir/install/sumi"
install_dir="$HOME/bin/sumi"
install_jar_name="sumi.jar"

src_sumi_jar=$(ls $src_jar_dir | grep sumi)
src_sumi_jar=$src_jar_dir/$src_sumi_jar

# prepare working directory to prepare install
rm -rf $tmp_install_dir && mkdir -p $tmp_install_dir

# build installation in temp location
# 1. install jar
cp $src_sumi_jar $tmp_install_dir/$install_jar_name
# 2. install run script
cp $script_dir/$sumi_run_script_name $tmp_install_dir
chmod u+x $tmp_install_dir/$sumi_run_script_name

# clear target install dir and install new copy of sumi
rm -rf $install_dir
mv $tmp_install_dir $install_dir
