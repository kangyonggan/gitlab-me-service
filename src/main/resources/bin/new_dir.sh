# 功能：创建目录
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名
# 参数4：分支名
# 参数5：父目录
# 参数6：目录名
# 参数7：提交信息
# 参数8：作者
# 参数9：邮箱

cd "${1}/${2}/${3}.git/worktree"

git pull
git checkout "${4}"

if [ ! -d $5 ]
then
  mkdir -p "${5}"
fi

cd "${5}"

pwd
if [ ! -d $6 ]
then
	 mkdir -p "${6}"
	 touch "${6}/.gitkeep"
	 git add .
	 git commit -m "${7}" --author="${8} <${9}>"
	 git push
fi


