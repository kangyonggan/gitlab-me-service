# 功能：压缩项目
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名
# 参数4：分支名
# 参数5：ZIP文件路径

cd "${1}/${2}/${3}.git/worktree"

git pull
git checkout "${4}"

cd "${1}/${2}/${3}.git"

zip -qr "${5}" worktree/
