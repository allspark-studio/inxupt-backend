# 在西邮后台开发规约

# git协作

> git地址：https://gitee.com/alkaidchen/zaixiyou

*由于github国内网络不稳定，选择`gitee`作为远程仓库。*

### 分支管理

- **master：** 项目生产环境代码，未来计划增加持续集成功能，所以`master`分支不要在`master`分支上进行开发性地提交，当确定`develop`分支稳定并且能够进入生产环境时，由我来 Merge 到`master`分支
- **develop：** 开发环境代码，所有开发代码从这个分支 Pull 到本地，然后再进行开发，开发之后 Pull Requests