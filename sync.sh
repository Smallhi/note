comment=$1
echo $comment

git add .
git commit -m "[#hhl $comment]"
git push 
