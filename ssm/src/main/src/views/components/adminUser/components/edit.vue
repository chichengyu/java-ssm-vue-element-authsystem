<template>
    <div class="add">
        <component-form :data="form" :width="80"></component-form>
    </div>
</template>
<script>
import { edit } from '@/api/adminUser'

export default {
    props:['show','groups'],
    data () {
        return {
            formData:null,
            form:{
                labelWidth:'200px',
                formFields:{
                    username: '',
                    telephone: '',
                    mail: '',
                    deptId: '',
                    status: '',
                    remark: '',
                },
                formLabel:[
                    {prop: 'username', title: '名称', type: 'input',placeholder:'请输入名称'},
                    {prop: 'telephone', title: '手机', type: 'input',placeholder:'请输入手机'},
                    {prop: 'mail', title: '邮箱', type: 'input',placeholder:'请输入邮箱'},
                    {prop: 'deptId', title: '部门', type: 'input',placeholder:'请输入部门'},
                    {prop: 'status', title: '状态', type: 'select',options:[
                            {label:'冻结',value:0},
                            {label:'正常',value:1},
                            {label:'删除',value:2},
                        ],change:(val) => {this.form.formFields.status = val;}
                    },
                    {prop: 'remark', title: '备注',type: 'input'},
                ],
                buttons:{
                    align:'center',
                    options:[
                        {title:'提交',type:'primary',loading:false,click:(form,item) => {
                            form.validate(valid => {
                                if (valid){
                                    item.loading = true;
                                    this.form.formFields.password && delete this.form.formFields.password;
                                    edit(this.form.formFields,this.form.formFields.id).then(res => {
                                        if (res.data.code == 1){
                                            this.success(res.data.msg);
                                            this.$emit('handleGetTableData');
                                        }else{
                                            this.error(res.data.msg);
                                        }
                                        setTimeout(()=>{
                                            item.loading = false;
                                        },2000);
                                    })
                                }
                            })
                        }}
                    ]
                },
                rules: {
                    username: [
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                    ],
                    telephone: [
                        { required: true, message: '请输入手机', trigger: 'blur' },
                        { pattern:this.validator.regExpPhone, message: '手机不正确', trigger: 'blur' }
                    ],
                    mail: [
                        { required: true, message: '请输入邮箱', trigger: 'blur' },
                        { pattern:this.validator.regExpEmail, message: '邮箱不正确', trigger: 'blur' }
                    ],
                    deptId: [
                        { required: true, message: '请输入部门', trigger: 'blur' },
                    ],
                    status: [
                        { required: true, message: '请选择状态', trigger: 'blur' },
                    ]
                }
            },
        }
    },
    methods:{
        currentData(currentData){
            this.$nextTick(() => {
                currentData&&(this.form.formFields = currentData);
            })
        }
    }
}
</script>
<style lang="css" scoped>

</style>