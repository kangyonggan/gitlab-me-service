# 功能：删除git项目
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名（如果没有项目名，删除命名空间下所有项目）

if [ $3 ]
then
  rm -rf "${1}/${2}/${3}.git"
else
  rm -rf "${1}/${2}/"
fi