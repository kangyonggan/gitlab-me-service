# 功能：创建文件
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名
# 参数4：分支名
# 参数5：父目录
# 参数6：文件目录
# 参数7：文件名
# 参数8：内容
# 参数9：提交信息
# 参数10：作者
# 参数11：邮箱

cd "${1}/${2}/${3}.git/worktree"

git pull
git checkout "${4}"
cd "${5}"

if [ $6 ]
then
	 mkdir -p "${6}"
	 cd "${6}"
fi

touch "$7"
echo "$8" > "$7"
git add .
git commit -m "${9}" --author="${10} <${11}>"
git push

