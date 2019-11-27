<template>
    <div class="groups">
        <div class="header">
            <el-button v-has="'add'" type="primary" @click="handleTable(true,'showTableAdd')" >添加</el-button>
        </div>
        <component-table :data="tableData">
            <template v-slot:button="{scope}">
                <el-button v-has="'auth'" type="success" size="mini" @click="handleAuth(scope)">分配权限</el-button>
                <el-button v-has="'edit'" type="primary" size="mini" @click="handleEdit(scope)">编辑</el-button>
                <component-popover v-has="'del'" :params="scope.row" @ok="handleOk" @cancel="handleCancel"></component-popover>
            </template>
        </component-table>

        <component-dialog v-if="show" :width="50" :title="optionText" :visible.sync="show" :footer="false">
            <!-- 添加 -->
            <table-add slot="dialog" v-if="showTable.showTableAdd" @handleGetTableData="handleGetTableData"></table-add>
            <table-edit slot="dialog" ref="tableEdit" v-if="showTable.showTableEdit" @handleGetTableData="handleGetTableData"></table-edit>
            <table-auth slot="dialog" ref="tableAuth" v-if="showTable.showTableAuth" @handleGetTableData="handleGetTableData" :rules="rules"></table-auth>
        </component-dialog>
    </div>
</template>

<script>
import tableAdd from './components/add.vue'
import tableEdit from './components/edit.vue'
import tableAuth from './components/auth.vue'
import { getTableData,edit,del } from '@/api/groups'

export default {
    name: "index",
    components:{
        tableAdd,
        tableEdit,
        tableAuth
    },
    data () {
        return {
            show:false,
            showTable:{
                showTableAdd:false,
                showTableEdit:false,
                showTableAuth:false,
            },
            currentPage:1,
            optionText:'',
            rules:null,
            search:{
                keywords:''
            },
            visible:false,
            tableData: {
                loading:false,
                // 请求回来的数据
                tableData:[],
                // 列
                tableLabel:[],
                // 操作
                tableOption:{
                    label:'操作',
                    width:250,
                    slot:true,
                    // buttons:[
                    //     {title:'分配权限',type:'success',click:(params) => {
                    //         this.handleTable(true, 'showTableAuth', '分配权限');
                    //         this.$nextTick(() => {
                    //             this.$refs.tableAuth && this.$refs.tableAuth.currentData(params.row)
                    //         })
                    //     }},
                    //     {title:'编辑',directives:[{name:'has',value:'edit'}],type:'primary',click: (params) => {
                    //         this.handleTable(true, 'showTableEdit', '编辑用户组');
                    //         this.$nextTick(() => {
                    //             this.$refs.tableEdit && this.$refs.tableEdit.currentData(params.row)
                    //         })
                    //     }},
                    //     {title:'删除',tooltip:true,directives:[{name:'has',value:'del'}],type:'danger',click:{
                    //         ok:(params) => {
                    //             del(params.row.id).then(res => {
                    //                if (res.data.code == 1){
                    //                    (this.tableData.tableData.length == 1 && this.currentPage > 1)&&(--this.currentPage);
                    //                    this.handleGetTableData();
                    //                    return this.success(res.data.msg);
                    //                }
                    //                return this.error(res.data.msg);
                    //             });
                    //         },
                    //         cancel:(params) => {this.warning('取消删除')}
                    //     }},
                    // ]
                },
                // 分页
                page:{align:'right',total:1,currentPage:1,currentChange:(currentPage) => {
                    this.currentPage = currentPage;
                    this.tableData.loading = true;
                    this.tableDataInit(this.currentPage,pageOffset,handleSearchData({...this.search}));
                }},
            },
        }
    },
    created(){
        this.handleGetTableData();
    },
    methods: {
        handleGetTableData(flag=false){
            this.handleTable(flag);
            this.labelInit();
            // this.tableDataInit(this.currentPage,pageOffset);
        },
        // 列初始化
        labelInit(){
            this.tableData.tableLabel = [
                {prop:'id',title:'ID',type:'index',fixed:true,width:60,align:'center'},
                {prop:'title',title:'名称'},
                {prop:'status',title:'状态',isSwitch:true,width:80,change:(currentData) => {
                    let txt = currentData.row.status==1?'启用用户组':'禁用用户组';
                    edit(currentData.row,currentData.row.id).then(res => {
                        if (res.data.code == 1){
                            return this.success(txt);
                        }
                        return this.error(res.data.msg);
                    });
                }}
            ];
        },
        // 数据初始化
        tableDataInit(currentPage,pageOffset,keywords='') {
            this.tableData.loading = true;
            getTableData(currentPage, pageOffset, keywords).then(res => {
                if (res.data.code == 1) {
                    this.tableData.tableData = res.data.data.data.data;
                    this.tableData.page.total = res.data.data.total;
                    this.rules = res.data.data.rules;
                } else {
                    this.jumpUrl(res.data,this);
                }
                this.tableData.loading = false;
            });
        },
        handleTable(flag,type='',text='添加用户组'){
            ObjectforIn(this.showTable,false);
            this.optionText = text;
            this.show = flag;
            this.showTable[type] = flag;
        },
        /****************************** 操作 ******************************/
        handleAuth(params){
            this.handleTable(true, 'showTableAuth', '分配权限');
            this.$nextTick(() => {
                this.$refs.tableAuth && this.$refs.tableAuth.currentData(params.row)
            })
        },
        handleEdit(params){
            this.handleTable(true, 'showTableEdit', '编辑用户组');
            this.$nextTick(() => {
                this.$refs.tableEdit && this.$refs.tableEdit.currentData(params.row)
            })
        },
        handleOk(params){
            del(params.id).then(res => {
                if (res.data.code == 1){
                    (this.tableData.tableData.length == 1 && this.currentPage > 1)&&(--this.currentPage);
                    this.handleGetTableData();
                    return this.success(res.data.msg);
                }
                return this.error(res.data.msg);
            });
        },
        handleCancel(params){
            return this.warning('取消删除');
        }
    },
}
</script>

<style lang="css" scoped>

</style>