$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/role',
        datatype: "json",
        colModel: [			
			{ label: '角色ID', name: 'id', index: 'role_id', width: 50, key: true },
			{ label: '角色名称', name: 'roleName', index: 'role_name', width: 80 }, 			
			{ label: '备注', name: 'remark', index: 'remark', width: 80 },
			{ label: '创建时间', name: 'gmtCreate', index: 'gmt_create', width: 80 },
			{ label: '最后修改时间', name: 'gmtModified', index: 'gmt_modified', width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "list",
            page: "currPage",
            total: "totalPage",
            records: "totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

//菜单树
var menu_ztree;
var menu_setting = {
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "parentId",
			rootPId: -1
		},
		key: {
			url:"nourl"
		}
	},
	check:{
		enable:true,
		nocheckInherit:true
	}
};

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		role: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.role = {};
			vm.getMenuTree(null);
		},
		update: function (event) {
			var roleId = getSelectedRow();
			if(roleId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getMenuTree(roleId);
		},
		saveOrUpdate: function (event) {
			//获取选择的菜单
			var nodes = menu_ztree.getCheckedNodes(true);
			var menuIdList = new Array();
			for(var i=0; i<nodes.length; i++) {
				menuIdList.push(nodes[i].id);
			}
			vm.role.menuIdList = menuIdList;

			var method = vm.role.id == null ? "POST" : "PUT";
			$.ajax({
				type: method,
			    url: "../sys/role",
			    contentType: "application/json",
			    data: JSON.stringify(vm.role),
			    success: function(r){
					alert('操作成功', function(index){
						vm.reload();
					});
				}
			});
		},
		del: function (event) {
			var roleIds = getSelectedRows();
			if(roleIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/role/delete-batch",
				    contentType: "application/json",
				    data: JSON.stringify(roleIds),
				    success: function(r){
						alert('操作成功', function(index){
							$("#jqGrid").trigger("reloadGrid");
						});
					}
				});
			});
		},
		getInfo: function(roleId){
			$.get("../sys/role/"+roleId, function(r){
                vm.role = r;
                //勾选角色所拥有的菜单
    			var menuIds = vm.role.menuIdList;
    			for(var i=0; i<menuIds.length; i++) {
    				var node = menu_ztree.getNodeByParam("id", menuIds[i]);
    				menu_ztree.checkNode(node, true, false);
    			}
            });
		},
		getMenuTree: function(roleId) {
			//加载菜单树
			$.get(baseURL + "sys/menu", function(r){
				menu_ztree = $.fn.zTree.init($("#menuTree"), menu_setting, r);
				//展开所有节点
				menu_ztree.expandAll(true);

				if(roleId != null){
					vm.getInfo(roleId);
				}
			});
	    },
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});