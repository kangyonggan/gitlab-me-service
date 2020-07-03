# 功能：创建git项目
# 参数1：git仓库根目录
# 参数2：命名空间
# 参数3：项目名

cd ${1}

if [ ! -d "$2" ]
then
 mkdir ${2}
fi

git init --bare ${2}/${3}.git

# clone
git clone "${2}/${3}.git" "${2}/${3}.git/worktree"
