### suppose that branch `master2` is going to replace master

git branch -m master old-master
git branch -m master2 master
git push -f origin master
