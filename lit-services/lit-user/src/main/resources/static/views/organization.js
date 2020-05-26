define(['Lit'], function (Lit) {
    var tmpl = `
<main class="aui-main">
    <app-breadcrumb title="组织管理"></app-breadcrumb>

    <div class="aui-main__bd">
        <div class="bg-white">
            <div class="pt-15 t-center">
                <el-button v-if="data.fullName" class="fz-xxl" type="text" @click="handleCorporation">
                    {{data.fullName}}
                </el-button>
                <!--<span v-if="data.fullName" class="fz-xxl">{{data.fullName}}</span>-->
                <template v-else>
                    <p class="fz-xl">企业信息尚未完善</p>
                    <el-button type="primary" @click="handleCorporation">去完善企业信息</el-button>
                </template>
            </div>
            <div class="sp-line sp-line--horizontal"></div> 
            
            <div class="padding-15">
                <el-row v-if="data.fullName" class="b-bottom-1" style="height: 60px;">
                    <el-col :span="4">
                        <el-button type="primary" plain icon="el-icon-plus" @click="handleAdd('')"></el-button>
                    </el-col>
                    <el-col :span="12" :offset="2">
                        <el-input v-model="keyword" placeholder="请输入搜索内容" v-on:keyup.native.enter="handleSearch">
                            <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
                        </el-input>
                    </el-col>
                </el-row>
    
                <el-row v-if="data.children" class="mt-15 b-bottom-1" style="height: 30px;">
                    <el-col :span="6"><span class="ml-25 fz-lg">名称</span></el-col>
                    <el-col :span="4"><span class="ml-25 fz-lg">编码</span></el-col>
                    <el-col :span="3"><span class="ml-15 fz-lg">顺序号</span></el-col>
                    <el-col :span="8"><span class="ml-15 fz-lg">地址</span></el-col>
                    <el-col :span="3"><span class="fz-lg">操作</span></el-col>
                </el-row>
                <el-tree v-if="data.children" :data="data.children"
                         :expand-on-click-node="false"
                         :filter-node-method="filterNode"
                         ref="orgTree">
                    <div slot-scope="{ node, data }" style="width: 100%">
                        <el-row type="flex" align="middle">
                            <el-col :span="6"><span>{{ data.fullName }}</span></el-col>
                            <el-col :span="4"><span>{{ data.code }}</span></el-col>
                            <el-col :span="3"><span>{{ data.orderNum }}</span></el-col>
                            <el-col :span="8"><span>{{ data.address }}</span></el-col>
                            <el-col :span="3">
                                <el-button type="text" icon="el-icon-plus" @click="handleAdd(data)"></el-button>
                                <el-button type="text" icon="el-icon-edit" @click="handleEdit(data)"></el-button>
                                <el-button type="text" icon="el-icon-delete" @click="handleDelete(data.id)"></el-button>
                            </el-col>
                        </el-row>
                    </div>
                </el-tree>
            </div>
        </div>
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
                <el-input v-model="editForm.fullName"></el-input>
            </el-form-item>
            <el-form-item label="顺序号">
                <el-input v-model="editForm.orderNum" type="number"></el-input>
            </el-form-item>
            <el-form-item label="地址">
                <el-input v-model="editForm.address"></el-input>
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
                    code: '',
                    fullName: '',
                    address: '',
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
                Lit.httpRequest.get('/api/org/list').then(res => {
                    this.data = res.result || {}
                })
            },
            handleCorporation() {
                redirect('/corporation/index')
            },
            handleAdd(parentNode) {
                this.editForm = {}
                this.editFormConfig.title = '新增组织'
                this.editFormConfig.parent = parentNode ? parentNode.fullName : this.data.fullName
                this.editFormConfig.isAdd = true
                this.editFormConfig.visible = true
                this.editForm.parentId = parentNode ? parentNode.id : this.data.id
            },
            handleEdit(node) {
                this.editForm.id = node.id
                this.editForm.code = node.code
                this.editForm.fullName = node.fullName
                this.editForm.address = node.address
                this.editForm.orderNum = node.orderNum
                this.editForm.remark = node.remark

                this.editFormConfig.title = '修改组织'
                this.editFormConfig.isAdd = false
                this.editFormConfig.visible = true
            },
            doEdit() {
                let method = this.editFormConfig.isAdd ? 'post' : 'put'
                Lit.httpRequest.request(method, '/api/org', this.editForm).then(res => {
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
                this.$confirm('此操作将删除该部门数据, 是否继续?', '提示', {
                    closeOnClickModal: false,
                    type: 'warning'
                }).then(() => {
                    Lit.httpRequest.delete('/api/org/' + id,).then(res => {
                        if (res.success) {
                            this.$message.success('删除部门成功')
                            this.initData()
                        } else {
                            this.$message.error(res.message)
                        }
                    })
                }).catch(() => {
                })
            },
            handleSearch() {
                this.$refs.orgTree.filter(this.keyword);
            },
            filterNode(value, data) {
                if (value) {
                    return (data.code && data.code.indexOf(value) !== -1)
                        || (data.fullName && data.fullName.indexOf(value) !== -1)
                        || (data.remark && data.remark.indexOf(value) !== -1)
                }
                return true;
            }
        }
    }
})