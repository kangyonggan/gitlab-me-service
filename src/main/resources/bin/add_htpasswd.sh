# 功能：添加用户名和密码到htpasswd
# 参数1：秘钥库父目录
# 参数2：用户名
# 参数3：密码

htpasswd -b "$1/gitlab.htpasswd" $2 $3