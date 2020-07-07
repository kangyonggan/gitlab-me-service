# 功能：删除文件
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名
# 参数4：分支名
# 参数5：文件全路径
# 参数6：提交信息
# 参数7：作者
# 参数8：邮箱

cd "${1}/${2}/${3}.git/worktree"

git pull
git checkout "${4}"

rm "${5}"
git add .
git commit -m "${6}" --author="${7} <${8}>"
git push

