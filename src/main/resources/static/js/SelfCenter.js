function newVerification() {
    let image = document.getElementById("verificationCode");
    image.src = "/jpetstore/verificationCode?" + new Date().getMilliseconds();
}

$(function () {
    // $('#password_old').on('blur', function () {
    //     let pwd = {
    //         password: $(this).val()
    //     }
    //
    // })
    console.log("response");
    let settings = {
        "url": "/jpetstore/user",
        "method": "GET",
        "timeout": 0,
        "headers": {
        },
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
        $('#nickname').val(response.data.nickname);
        $('#account').val(response.data.account);
    });
});

function changeName(){
    let newName = $('#nickname').val()
    let settings = {
        "url": `/jpetstore/user/info?nickname=${newName}`,
        "method": "PUT",
        "timeout": 0,
        "headers": {

        },
    };
    $.ajax(settings).done(function (response) {
       if(response.code===200){
           alert("修改成功")
       }
       else{
           alert("修改失败")
       }
    });
}

function reset(){
    $('#oldPassword').val('')
    $('#password').val('')
    $('#rePassword').val('')
    $('#code').val('')
}

function changePwd(){
    let oldPassword = $('#oldPassword').val()
    let password = $('#password').val()
    let rePassword = $('#rePassword').val()
    let code = $('#code').val()
    let id = getCookie('CaptchaCode');
    RemoveCookie('CaptchaCode');
    let settings = {
        "url": "/jpetstore/user/auth",
        "method": "PUT",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            oldPassword: oldPassword,
            password: password,
            rePassword: rePassword,
            id: id,
            code: code
        }),
    };

    $.ajax(settings).done(function (response) {
        console.log(response.code);
        if(response.code===200){
            alert("密码修改成功")
        }
        else{
            newVerification()
            $('#errorMessage').text(response.data);
        }
    });
}

function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(";");
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
}

function RemoveCookie(cookieName) {
    let cookies = document.cookie.split(";");//将所有cookie键值对通过分号分割为数组
    //循环遍历所有cookie键值对
    for (let i = 0; i < cookies.length; i++) {
        //有些cookie键值对前面会莫名其妙产生一个空格，将空格去掉
        if (cookies[i].indexOf(" ") == 0) {
            cookies[i] = cookies[i].substring(1);
        }
        //比较每个cookie的名称，找到要删除的那个cookie键值对
        if (cookies[i].indexOf(cookieName) == 0) {
            let exp = new Date();//获取客户端本地当前系统时间
            exp.setTime(exp.getTime() - 60 * 1000);//将exp设置为客户端本地时间1分钟以前，将exp赋值给cookie作为过期时间后，就表示该cookie已经过期了, 那么浏览器就会将其立刻删除掉
            document.cookie = cookies[i] + ";expires=" + exp.toUTCString();//设置要删除的cookie的过期时间，即在该cookie的键值对后面再添加一个expires键值对，并将上面的exp赋给expires作为值(注意expires的值必须为UTC或者GMT时间，不能用本地时间），那么浏览器就会将该cookie立刻删除掉
            //注意document.cookie的用法很巧妙，在对其进行赋值的时候是设置单个cookie的信息，但是获取document.cookie的值的时候是返回所有cookie的信息
            break;//要删除的cookie已经在客户端被删除掉，跳出循环
        }
    }
}