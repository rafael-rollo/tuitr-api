#!/bin/bash

# Print version name injected by semantic release plugin
echo "version = $1"

# Get version number from version tag
JAR_VERSION=`echo $1 | cut -d'v' -f2`

echo "jar = $JAR_VERSION"

# Set new version in pom.xml using mvn versions:set command
# These new pom.xml and changelog.md generated by @semantic-release/changelog
# will be commit it by @semantic-release/git
mvn versions:set -DnewVersion=$JAR_VERSION

# Package the new version
mvn -B -DskipTests package --file pom.xml

# Copy it to release folder
# These files will be upload to github by @semantic-release/github
mkdir release && cp target/*.jar release

printenv