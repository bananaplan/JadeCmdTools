# JadeGuiTools
青鸟教学工具

## 如何使用
**1.  主界面**

<img src="https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/home.png" width="600" />

**2.  设置班级和学员信息**

第一次使用时，需要先设置班级和学员信息，用于 **随机点名** 和 **编码竞赛** 功能，如果没有进行此项设置，则以上2个功能不可用，且使用 **作业分析** 功能时，需要输入以学员名字命名的文件夹的深度。

从 **Config** 菜单进入设置界面：

![](https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/menu_config_class.png)

设置班级和学员信息界面：

![](https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/config_class.png)

在 **班级名称** 的下拉框中，输入要添加的班级名称，在 **学员列表** 的文本框中，输入对应班级的学员信息：学号 姓名（中间用 空格 或 制表符 隔开），最后保存。

**3.  选择班级**

每次打开软件，或第一次设置完班级和学员信息后，都需要选择班级，使后续操作都基于当前选中的班级进行。

![](https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/select_class.png)

除了选择设置好的班级以外，还可以选择 **其他班级**，当选此项时，由于不能获取学员信息，所以无法使用 随机点名 和 编码竞赛 等功能。

**4.  随机点名**

从 **Tools** 菜单，选择 **幸运抽奖** 进入，用于随机生成学员姓名，课堂点名提问。生成的学员姓名不会与已点过名的重复，也就是说，一个学员只能被点一次，全部轮完后，则重新开始。也可以点击界面右下角的 **重置**，重新开始新一轮点名。

![](https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/lucky.png)

**5.  编码竞赛**

用于记录学员上机完成情况，按照完成速度排序。

![](https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/competition_usage.png)

用法：
1.  先输入上机练习标题，点击 **开始竞赛**，界面右下角就会出现当前所用时间。
2.  当学员完成上机后，报其学员编号，将编号输入 **学员编号** 文本框，点击 **回车** （或点击 提交 按钮），完成记录会立即出现在下面列表中。
3.  可以通过下拉框选择查看当日的上机完成情况，由于所有的数据，均以普通文本的形式保存，也可进入 **report文件夹** 自行查看详情。

**6.  作业分析**

分析录屏和作业相似度。

![](https://github.com/bananaplan/JadeCmdTools/raw/gui/images_help/homework_usage.png)

用法：
1.  先输入或选择作业目录。
2.  如果没有选择班级，或选的是其他班级，则需要输入以学员姓名命名的文件夹的深度，用于定位学员姓名。
3.  可以输入过滤关键字，用来忽略某些目录和文件，多个关键字用 **空格** 隔开。
4.  点击 **开始分析**，分析完成后，结果会显示在下方列表。
5.  **双击** 列表中的某一条，查看代码差异，也可以选中某一条，点击下方的 **显示差异** 和 **显示详情** 按钮来查看差异。
6.  点击 **录屏详情**，查看录屏情况。

**7.  笔试选择题批改**

待开发...
