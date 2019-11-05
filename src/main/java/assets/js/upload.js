$(document).ready(function () {
	var module = $('#fileupload').attr('data-module');
		$('#fileupload').fileupload({
	       	dataType: 'json',
	        maxNumberOfFiles: 1,
	        enctype: "multipart/form-data",
	        type: "post",
	        formData: {module: module},
	        send: function(e,data){
	            var $file = data.files[0].name;
	            if (!/\.(gif|jpg|jpeg|png)$/i.test($file)) {
	                alert('请上传jpg、jpeg、gif、png格式的图片');
	                        return false;
	                }
	            return true;
	        },
	        done: function (e, data) {
	        	$('#attach').val(data.result.name);
	        	$('.attach').attr({'href': '/image/'+ data.result.module + '/' +data.result.name + '/dash_detail', 'target': "_blank"});
	            $('.attach').html('<img src="/image/'+ data.result.module + '/' +data.result.name + '/dash_detail" width="500px" />');
	            $('.J-upload').html('重新上传');
	        }
	    });

	$('.J-upload').click(function() {
		$('#fileupload').trigger("click");
	})
})