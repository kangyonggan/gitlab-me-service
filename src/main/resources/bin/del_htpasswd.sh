# 功能：删除htpasswd
# 参数1：秘钥库父目录
# 参数2：用户名

htpasswd -D "$1/gitlab.htpasswd" $2
