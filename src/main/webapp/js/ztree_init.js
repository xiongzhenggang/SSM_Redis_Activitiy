/**
 * 初始化菜单树形结构
 */
function onLoadZTree(url){
  var treeNodes;
  $.ajax({
    async:false,//是否异步
    cache:false,//是否使用缓存
    type:'POST',//请求方式：post
    dataType:'json',//数据传输格式：json
    url:url,//请求的action路径
    error:function(){
      //请求失败处理函数
      alert('亲，请求失败！');
    },
    success:function(data){
      //console.log(data);
      //请求成功后处理函数
      treeNodes = data;//把后台封装好的简单Json格式赋给treeNodes
    }
  });
  var zTree;
  var setting = {
    view: {
      dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
      txtSelectedEnable: true,//分别表示 允许 / 不允许 选择 zTree Dom 内的文本
      showLine: false,//是否显示节点之间的连线
      fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
      selectedMulti: false //设置是否允许同时选中多个节点
    },
    check:{
      //chkboxType: { "Y": "ps", "N": "ps" },
      chkStyle: "checkbox",//复选框类型
      enable: false//每个节点上是否显示 CheckBox 
    },
    data: {
      simpleData: {//简单数据模式
        enable:true,
        idKey: "id",
        pIdKey: "pId",
        rootPId: ""
      },
      key:{
    	  checked: "id",
    	  target:"target" ,//指向url的target
    	  url:"url"
      }
    },
    callback: {//回调函数
      	beforeClick: function(treeId, treeNode) {
        zTree = $.fn.zTree.getZTreeObj("user_tree");
        //  zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
        zTree.expandAll(true); //修改为展开所有
        return true;
      }
    },
    onClick: function(treeId,treeNode){
    	treeNode.open();
    }
  };
  var t = $("#user_tree");
  t = $.fn.zTree.init(t,setting,treeNodes);
}