# 功能：更新文件
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名
# 参数4：分支名
# 参数5：父目录
# 参数6：文件目录
# 参数7：文件名
# 参数8：原文件名
# 参数9：临时文件路径（存放内容）
# 参数10：提交信息
# 参数11：作者
# 参数12：邮箱

cd "${1}/${2}/${3}.git/worktree"

git pull
git checkout "${4}"

if [ ! -d $5 ]
then
  mkdir -p "${5}"
fi

cd "${5}"

if [[ "$6" || "$8" != "$7" ]]
then
  if [ $6 ]
  then
     mkdir -p "${6}"
  fi

  mv "$8" "$6$7"
fi

cat "$9" > "$7"

git add .
git commit -m "${10}" --author="${11} <${12}>"
git push

