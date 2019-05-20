define(['Lit'], function (Lit) {
    let tmpl = `
<main class="aui-main">
    <app-breadcrumb title="字典详情"></app-breadcrumb>

    <div class="aui-main__bd">
        <el-card shadow="never">
            <div slot="header">
                <el-row>
                    <el-col :span="4">
                        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd('')"></el-button>
                    </el-col>
                    <el-col :span="12" :offset="2">
                        <el-input v-model="keyword" placeholder="请输入搜索内容" @keyup.native.enter="handleSearch">
                            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                        </el-input>
                    </el-col>
                </el-row>
            </div>

            <el-row v-if="data" class="b-bottom-1" style="height: 30px;">
                <el-col :span="5"><span class="ml-25 fz-lg">{{ data.dictKey }}</span></el-col>
                <el-col :span="5"><span class="ml-25 fz-lg">{{ data.dictValue }}</span></el-col>
                <el-col :span="10"><span class="ml-15 fz-lg">{{ data.remark }}</span></el-col>
                <el-col :span="4"><span class="fz-lg">操作</span></el-col>
            </el-row>
            <el-tree v-if="data.children" :data="data.children"
                     :expand-on-click-node="false" :filter-node-method="filterNode"
                     default-expand-all ref="dictTree">
                <div slot-scope="{ node, data }" class="w-100">
                    <el-row type="flex" align="middle">
                        <el-col :span="5"><span>{{ data.dictKey }}</span></el-col>
                        <el-col :span="5"><span>{{ data.dictValue }}</span></el-col>
                        <el-col :span="10"><span>{{ data.remark }}</span></el-col>
                        <el-col :span="4">
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
            <el-form-item label="字典key">
                <el-input v-model="editForm.dictKey"></el-input>
            </el-form-item>
            <el-form-item label="字典value">
                <el-input v-model="editForm.dictValue"></el-input>
            </el-form-item>
            <el-form-item label="顺序号">
                <el-input v-model="editForm.orderNum" type="number"></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input type="textarea" :rows="2" v-model="editForm.remark"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer">
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
                dictId: singlePage ? this.$route.params.id : getCurrentPathVariable(),
                data: {},
                keyword: '',
                editFormConfig: {
                    visible: false,
                    isAdd: true,
                    title: '',
                    parent: ""
                },
                editForm: {
                    id: 0,
                    dictKey: '',
                    dictValue: '',
                    remark: '',
                    orderNum: ''
                }
            }
        },
        created() {
            Lit.appendStyle('.el-tree-node__content {height: 45px;border-bottom: 1px solid #ebeef5;}')
            this.initData()
        },
        methods: {
            initData() {
                Lit.httpRequest.get('/api/dictionary/detail/' + this.dictId).then(res => {
                    this.data = res.result || {}
                })
            },
            handleAdd(parentNode) {
                this.editForm = {}
                if (!parentNode) {
                    parentNode = this.data
                }
                this.editFormConfig.title = '新增字典'
                this.editFormConfig.parent = parentNode.dictKey + ' -- ' + parentNode.dictValue
                this.editFormConfig.isAdd = true
                this.editFormConfig.visible = true
                this.editForm.parentId = parentNode.id
            },
            handleEdit: function (node) {
                this.editForm.id = node.id
                this.editForm.dictKey = node.dictKey
                this.editForm.dictValue = node.dictValue
                this.editForm.orderNum = node.orderNum
                this.editForm.remark = node.remark

                this.editFormConfig.title = '修改字典'
                this.editFormConfig.isAdd = false
                this.editFormConfig.visible = true
            },
            doEdit() {
                let method = this.editFormConfig.isAdd ? 'post' : 'put'
                Lit.httpRequest.request(method, '/api/dictionary', this.editForm).then(res => {
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
                this.$confirm('此操作将删除该字典数据, 是否继续?', '提示', {
                    closeOnClickModal: false,
                    type: 'warning'
                }).then(() => {
                    Lit.httpRequest.delete('/api/dictionary/' + id,).then(res => {
                        if (res.success) {
                            this.$message.success('删除字典成功')
                            this.initData()
                        } else {
                            this.$message.error(res.message)
                        }
                    })
                }).catch(() => {
                })
            },
            handleSearch() {
                this.$refs.dictTree.filter(this.keyword);
            },
            filterNode(value, data) {
                if (value) {
                    return (data.dictKey && data.dictKey.indexOf(value) !== -1)
                        || (data.dictValue && data.dictValue.indexOf(value) !== -1)
                        || (data.remark && data.remark.indexOf(value) !== -1)
                }
                return true;
            }
        }
    }
})