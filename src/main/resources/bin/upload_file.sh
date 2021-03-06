# 功能：上传文件
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名
# 参数4：分支名
# 参数5：父目录
# 参数6：文件名
# 参数7：原文件
# 参数8：提交信息
# 参数9：作者
# 参数10：邮箱

cd "${1}/${2}/${3}.git/worktree"

git pull
git checkout "${4}"

if [ ! -d $5 ]
then
  mkdir -p "${5}"
fi

mv "${7}" "${5}/${6}"
git add .
git commit -m "${8}" --author="${9} <${10}>"
git push

