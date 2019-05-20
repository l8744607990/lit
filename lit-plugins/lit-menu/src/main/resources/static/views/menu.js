define(['Lit'], function (Lit) {
    let tmpl = `
<main class="aui-main">
    <app-breadcrumb title="菜单管理"></app-breadcrumb>

    <div class="aui-main__bd">
        <el-card shadow="never">
            <div slot="header">
                <el-row>
                    <el-col :span="4">
                        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd('')"></el-button>
                    </el-col>
                    <el-col :span="12" :offset="2">
                        <el-input v-model="keyword" placeholder="请输入搜索内容" v-on:keyup.native.enter="handleSearch">
                            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                        </el-input>
                    </el-col>
                </el-row>
            </div>

            <el-row class="b-bottom-1" style="height: 30px;">
                <el-col :span="4"><span class="ml-25 fz-lg">编码</span></el-col>
                <el-col :span="2"><span class="ml-15 fz-lg">图标</span></el-col>
                <el-col :span="4"><span class="ml-25 fz-lg">名称</span></el-col>
                <el-col :span="6"><span class="ml-15 fz-lg">url</span></el-col>
                <el-col :span="2"><span class="fz-lg">顺序号</span></el-col>
                <el-col :span="3"><span class="fz-lg">启用状态</span></el-col>
                <el-col :span="3"><span class="fz-lg">操作</span></el-col>
            </el-row>
            <el-tree :data="data"
                     :expand-on-click-node="false"
                     :filter-node-method="filterNode"
                     ref="menuTree">
                <div slot-scope="{ node, data }" style="width: 100%;height: 45px;border-bottom: 1px solid #ebeef5;">
                    <el-row type="flex" align="middle">
                        <el-col :span="4"><span>{{ data.code }}</span></el-col>
                        <el-col :span="2"><i :class="data.icon"></i></el-col>
                        <el-col :span="4"><span> {{ data.name }}</span></el-col>
                        <el-col :span="6"><span>{{ data.url }}</span></el-col>
                        <el-col :span="2"><span>{{ data.orderNum }}</span></el-col>
                        <el-col :span="3">
                            <el-switch v-model="data.enable" @change="handleChange(data.id)"></el-switch>
                        </el-col>
                        <el-col :span="3">
                            <el-button type="text" icon="el-icon-plus" @click="handleAdd(data)"></el-button>
                            <el-button type="text" icon="el-icon-edit" @click="handleEdit(data)"></el-button>
                            <el-button type="text" icon="el-icon-delete" @click="handleDelete(data.id)"></el-button>
                        </el-col>
                    </el-row>
                </div>
            </el-tree>
        </el-card>
    </div>

    <el-dialog :title="editFormConfig.title" :visible.sync="editFormConfig.visible" :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" label-suffix=":">
            <el-form-item label="父节点" v-if="editFormConfig.isAdd">
                <span>{{editFormConfig.parent}}</span>
            </el-form-item>
            <el-form-item label="编码">
                <el-input v-model="editForm.code"></el-input>
            </el-form-item>
            <el-form-item label="名称">
                <el-input v-model="editForm.name"></el-input>
            </el-form-item>
            <el-form-item label="图标">
                <el-input v-model="editForm.icon"></el-input>
            </el-form-item>
            <el-form-item label="url">
                <el-input v-model="editForm.url"></el-input>
            </el-form-item>
            <el-form-item label="顺序号">
                <el-input v-model="editForm.orderNum" type="number"></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input type="textarea" :rows="2" v-model="editForm.remark"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="doEdit">确 定</el-button>
            <el-button @click="editFormConfig.visible = false">取 消</el-button>
        </div>
    </el-dialog>
</main>
    `
    return {
        template: tmpl,
        data: function () {
            return {
                data: [],
                keyword: '',
                editFormConfig: {
                    visible: false,
                    isAdd: true,
                    title: '',
                    parent: ""
                },
                editForm: {
                    id: 0,
                    code: '',
                    name: '',
                    url: '',
                    icon: '',
                    orderNum: '',
                    remark: ''
                }
            }
        },
        created() {
            Lit.appendStyle('.el-tree-node__content {height: 45px;border-bottom: 1px solid #ebeef5;}')
            this.initData()
        },
        methods: {
            initData() {
                Lit.httpRequest.get('/api/menu/tree').then(res => {
                    this.data = res.result || []
                })
            },
            handleAdd(parentNode) {
                this.editForm = {}
                this.editFormConfig.title = '新增菜单'
                this.editFormConfig.parent = parentNode ? parentNode.code + ' -- ' + parentNode.name : '--'
                this.editFormConfig.isAdd = true
                this.editFormConfig.visible = true
                this.editForm.parentId = parentNode ? parentNode.id : 0
            },
            handleEdit(node) {
                this.editForm.id = node.id
                this.editForm.code = node.code
                this.editForm.name = node.name
                this.editForm.url = node.url
                this.editForm.icon = node.icon
                this.editForm.orderNum = node.orderNum
                this.editForm.remark = node.remark

                this.editFormConfig.title = '修改菜单'
                this.editFormConfig.isAdd = false
                this.editFormConfig.visible = true
            },
            doEdit() {
                let method = this.editFormConfig.isAdd ? 'post' : 'put'
                Lit.httpRequest.request(method, '/api/menu', this.editForm).then(res => {
                    if (res.success) {
                        this.$message.success(this.editFormConfig.title + '成功')
                        this.initData()
                    } else {
                        this.$message.error(res.message)
                    }
                })
                this.editFormConfig.visible = false
            },
            handleDelete(id) {
                this.$confirm('此操作将删除该菜单数据, 是否继续?', '提示', {
                    closeOnClickModal: false,
                    type: 'warning'
                }).then(() => {
                    Lit.httpRequest.delete('/api/menu/' + id,).then(res => {
                        if (res.success) {
                            this.$message.success('删除菜单成功')
                            this.initData()
                        } else {
                            this.$message.error(res.message)
                        }
                    })
                }).catch(() => {
                })
            },
            handleChange(id) {
                Lit.httpRequest.post("/api/menu/change/status/" + id).then(res => {
                    if(!res.success) {
                        this.$message.error(res.message)
                    }
                })
            },
            handleSearch() {
                this.$refs.menuTree.filter(this.keyword);
            },
            filterNode(value, data) {
                if (value) {
                    return (data.code && data.code.indexOf(value) !== -1)
                        || (data.name && data.name.indexOf(value) !== -1)
                        || (data.remark && data.remark.indexOf(value) !== -1)
                }
                return true;
            }
        }
    }
})