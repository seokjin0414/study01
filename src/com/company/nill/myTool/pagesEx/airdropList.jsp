<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <%@ include file="../include/common_admin_head.jsp" %>
    <jsp:include page="../include/inc_lnb.jsp">
        <jsp:param name="menu_1depth" value="03"/>
        <jsp:param name="menu_2depth" value="09"/>
    </jsp:include>
</head>
<body>

<div class="containerWrap">
    <div class="container">
        <div class="top clearfix">
            <ul class="menu_dept clearfix">
                <li>BIO관리</li>
                <li>AIR DROP 내역</li>
            </ul>
        </div>
        <div class="current tabContent">
            <div class="cn_wrap">
                <div class="table_top">
                    <span class="table_sbj">검색</span>
                </div><!--// table_top -->
                <table class="horizon_tbl">
                    <colgroup>
                        <col width="10%">
                        <col width="*">
                        <col width="10%">
                        <col width="*">
                    </colgroup>
                    <tbody>
                    <tr>
                        <th>조건</th>
                        <td>
                            <select id="statusVal">
                                <option value="">진행상태</option>
                                <option value="R">대기</option>
                                <option value="W">진행중</option>
                                <option value="C">완료</option>
                                <option value="F">실패</option>
                            </select>
                            <select id="coinVal">
                                <option value="">코인종류</option>
                                <option value="LBIO">LBIO</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>검색</th>
                        <td>
                            <select id="searchField">
                                <option value="">선택</option>
                                <option value="usrIdx">받은사람 usrIdx</option>
                                <option value="usrId">받은사람 ID</option>
                                <option value="usrNick">받은사람 이름</option>
                                <option value="refCd">받은사람 refCd</option>
                                <option value="sendAddr">보낸사람 주소</option>
                                <option value="receiveAddr">받는사람 주소</option>
                            </select>
                            <input type="text" id="searchVal" style="width: 300px;">
                        </td>
                    </tr>
                    </tbody>
                </table>
                <a href="#n" class="btn_c dis_b" id="btnSearch">검 색</a>
            </div>
            <div class="cn_wrap srch_result">
                <div class="table_top clearfix">
                </div>
                <div class="table_selected clearfix mb7">
                    <p class="fl_left">
                        <a class="btn_gray" id="btnUpload">AIR DROP 파일업로드</a>
                    <form id="uploadForm">
                        <input type="file" name="file" id="file" style="display: none" onchange="excelUpload(this)"/>
                    </form>
                    <p class="fl_left">
                        <a class="btn_gray" id="airdropWithR">대기건 지급</a>
                    </p>
                    <p class="fl_left">
                        <a class="btn_red" id="airdropWithF">실패건 재지급</a>
                    </p>
                    <p class="fl_right">
                        <a class="btn_r02_green" href="/assets/file/event_airdrop.xlsx">양식다운로드</a>
                    </p>
                    <p class="fl_right">
                        <a class="btn_r02_green" id="btnExcel">엑셀다운로드</a>
                    </p>
                </div>
                <div id="jsGrid">
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script>

    function excelUpload(file) {
        var form = $('#uploadForm')[0];
        var formData = new FormData(form);

        $.ajax({
            url: '/api/v1/bio/insertTnEventAirdropByExcel',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false
        }).done(function (json) {
            if (json.code == '200') location.reload();
            else alert(json.message);
        });
    }

    var pageAction = function () {

        var init = function () {
            tableRender();
            eventBind();
        }

        var eventBind = function () {
            $('#btnSearch').click(function (event) {
                event.preventDefault();
                $("#jsGrid").jsGrid("reset");
            });

            $("#searchVal").keydown(function (e) {
                if (e.keyCode == 13) {
                    $('#btnSearch').click();
                }
            });

            $("#btnExcel").click(function () {
                var url = '/api/v1/bio/tnEventAirdropListExcel';
                url += '?searchVal=' + $('#searchVal').val();
                url += '&searchField=' + $('#searchField').val();
                url += '&statusVal=' + $('#statusVal').val();
                url += '&coinVal=' + $('#coinVal').val();
                location.href = url;
            });

            $('#btnUpload').click(function () {
                $('#file').click();
            });

            //  대기건 (R) airdrop
            $('#airdropWithR').click(function () {
                if (confirm("대기 상태의(R) AIRDROP을 진행하시겠습니까 ?")) {
                    var param = {
                    };

                    postJson('/api/v1/bio/airdropWithStatusW', param, function (json) {
                        if (json.code == '200') {
                            alert("AIRDROP 을 진행하였습니다.");
                            location.reload();
                        } else {
                            alert(json.message);
                        }
                    });
                }
            });

            //  실패건 (F) airdrop
            $('#airdropWithF').click(function () {
                if (confirm("실패 상태의(F) AIRDROP을 다시 진행하시겠습니까 ?")) {
                    var param = {
                    };

                    postJson('/api/v1/bio/airdropWithStatusF', param, function (json) {
                        if (json.code == '200') {
                            alert("AIRDROP 을 진행하였습니다.");
                            location.reload();
                        } else {
                            alert(json.message);
                        }
                    });
                }
            });

        };

        var tableRender = function () {
            jsGridInit();

            $('#jsGrid').jsGrid({
                controller: {
                    loadData: function loadData(filter) {
                        var d = $.Deferred();
                        var param = {
                            searchField : $('#searchField').val(),
                            searchVal : $('#searchVal').val(),
                            statusVal   : $('#statusVal').val(),
                            coinVal     : $('#coinVal').val()
                        };
                        $.extend(filter, param);
                        postJson('/api/v1/bio/tnEventAirdropList', filter, function (json) {
                            if (json.code != '200') {

                            }
                            d.resolve(json);
                        });
                        return d.promise();
                    }
                },
                fields: [{
                    title: 'IDX',
                    name: "airdropIdx",
                    align: "center",
                    width: 40
                }, {
                    title: '통화구분/코드',
                    align: "center",
                    width: 70,
                    itemTemplate: function itemTemplate(value, row) {
                        return row.crncyType + "/" + row.crncyCd;
                    }
                }, {
                    title: '수량',
                    name: "volume",
                    align: "center",
                    width: 100,
                    itemTemplate: function itemTemplate(value, row) {
                        return Math.floor(value * 100000000) / 100000000;
                    }
                }, {
                    title: '받는사람 IDX',
                    name: "usrIdx",
                    align: "center",
                    width: 50
                }, {
                    title: '받는사람 ID',
                    name: "usrId",
                    align: "center",
                    width: 160
                }, {
                    title: '받는사람 refCd',
                    name: "refCd",
                    align: "center",
                    width: 100
                }, {
                    title: '받는사람 주소',
                    name: "receiveAddr",
                    align: "center",
                    width: 320
                }, {
                    title: '내부 txId',
                    name: "txId",
                    align: "center",
                    width: 300
                }, {
                    title: '외부 txId',
                    name: "extTxId",
                    align: "center",
                    width: 100,
                    itemTemplate: function itemTemplate(value,item) {
                        var a = value
                        if (a != null) {
                            a = a.substr(0,4) + '....' + a.slice(-4)
                        }
                        var txt = genAnchor('javascript:void(0);',a).css("text-decoration", "underline").css("color", "blue");
                        txt.click(function(){
                            var options = 'top=50, left=50, width=1060, height=1000, status=no, menubar=no, toolbar=no, resizable=no';
                            window.open('https://scan.1111111.com/tx/' + value + '/token-transfers', 'popup', options);
                        });
                        return txt;
                    }
                }, {
                    title: '보내는사람 IDX',
                    name: "sendUsrIdx",
                    align: "center",
                    width: 50
                }, {
                    title: '보내는사람 주소',
                    name: "sendAddr",
                    align: "center",
                    width: 320
                }, {
                    title: '코멘트',
                    name: "comment",
                    align: "center",
                    width: 100
                }, {
                    title: '상태',
                    name: "status",
                    align: "center",
                    width: 40,
                    itemTemplate : itemStatusTemplate
                }, {
                    title: '등록일자',
                    name: "regDtm",
                    width: 100,
                    align: "center"
                }, {
                    title: '수정일자',
                    name: "modDtm",
                    width: 100,
                    align: "center"
                }]
            });
        };

        function itemStatusTemplate(value) {
            var status = value;
            if (status == 'R') {
                status = "대기";
            } else if (status == 'W') {
                status = "진행중";
            } else if (status == 'C') {
                status = "완료";
            } else if (status == 'F') {
                status = "실패";
            }
            return status;
        }

        return {
            init: init
        }

    }

    $(function () {
        pageAction().init();
    });
</script>
</html>