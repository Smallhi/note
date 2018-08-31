#!/usr/bin/env bash
comment=$*
echo $comment

git add .
git commit -m "[#hhl $comment]"
git push


