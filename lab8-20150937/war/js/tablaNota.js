

/*En cuentra el elemento de la fila del button +1 fila siguiente*/
//Donde td es el button de insertar
function filaInsertar(td) {
    var fila = $(td).parent().parent();
    var numFil = $(fila).parent().children().index($(fila));
    /*Para encontrar la ubicacion donde Trabajar la clase rpta*/
    return $(fila).siblings()[numFil];
}
/*Devuleve los tr donde esta los datos reales en un array*/
/*Donde td es el rpta*/
function filaAnterior(rpta) {
    var numFil = $(rpta).parent().children().index($(rpta));
    var fila = $(rpta).parent().children();
    var datos = $(fila[numFil - 1]).children();
    var array = new Array(3);
    for (var i = 2; i <= 4; i++)
        array[i - 2] = $(datos[i]);
    return array;
}

/*Me devuelve una array con los datos de la fila button insertar
 * donde los datos se enceuntra en tr en la pos  2 3 4*/
function filaDatos(td) {
    var array = new Array(3);
    var datos = $(td).parent().siblings();
    for (var i = 2; i <= 4; i++)
        array[i - 2] = $(datos[i]).text();
    return array;
}
function inputFila(rpta) {
    /*Estas contiene todos los tr de la fila rpta voy a obtener todos los inputs */
    var tr = $(rpta).children();
    var array = new Array(3);
    for (var i = 2; i <= 4; i++)
        array[i - 2] = $(tr[i]).children()[0];
    return array;
}

/*Llena los datos en la fila*/

$(document).ready(function () {

    /*Crea un fila para completar los datos en un pequeno formulario con un button y agrega un evento a dicho button */
    $('.insertar').click(function () {
        /*Copia los datos almacenados en un el formulario para mostrarlos*/
        var rpta = filaInsertar(this);
        var dato = filaDatos(this);
        var input = inputFila(rpta);
        for (var i = 0; i < dato.length; i++) {
            input[i].value = dato[i];
            $(input[i]).attr('disabled', true);
        }
        $(rpta).show();
    });
    $(".cancelar").click(function () {
        /*Oculta el contenido y no es necesario borra porque cuando quiera verlo .insertar cambia los valores */
        var rpta = filaInsertar(this);
        $(rpta).hide();
    });
    $(".actualizar").click(function () {
  /*Prev hermano anterio de rpta el td codigo, nombre, nota 01,nota 02, nota 03*/
        var rpta = $(this).parent().parent();
        var array = filaAnterior(rpta);
        var input = inputFila(rpta);
        var curCod=$('#curCod').text();
      
        var tr=$(this).parent().parent().prev().children();
        var alumno=$(tr[0]).text();
        /* Creo el Objeto JSON mas adelante anter en datos correctos si este esta vacion el campo ponerlo cambiarlo a null
         * en la BD si no genera el valor por defecto CERO*/
       //No se puede enviar un valor null como parametro al menos no lo aceptaba y si lo intenamso no manda la 
       // name='null' que es != name=null;
       /*for(var i=0;i<input.length;i++)
           if(input[i].value===""){
               input[i].value=null;
            }*/
        
        $.ajax({
                     method:'get',
                     url:'tablaNota',
                     data:{
                         n1:input[0].value,n2:input[1].value,n3:input[2].value,curCod:curCod,alumCod:alumno
                     },
                     //Son los datos del servidor 
                     /*beforeSend(Function)*/
                     success:function(data){
                         /*Los datos son correctoz y se a insetado en la BD*/
                      if(data==="ok"){
                          console.log(data);
                          /*Como los datos ya estan en BD se procedesn a actualizar en LA TABLA con JS sin NECESIDAD DE
                           * cargar de nuevo todo lso datos de LA BD , actuliza la BD actualiza en la pantalla con JS*/
                        for (var i = 0; i < array.length; i++) {
                         $(array[i]).text(input[i].value);
                        }
                    }
                    else {
                        window.alert("Se produjo el error en LA actuazlicion de datos en BD");
                        console.log(data);
                    }
                     $(rpta).hide();
                     },
                     error:function(data){
                         /*Erroe 404 , hay un parametro par tiempo de espera se sobreapaso ese tiempo de espera etc
                          * no logro la conexion con el servidor entr otros*/
                         console.log(data);
                     }
                 });
        
        
        
    });
    /*El evento se desarrolla sobre tr y activa la celda*/
    $(".valor").parent().dblclick(function () {

        var valor = $(this).children()[0];
        $(valor).attr('disabled', false);

    });
});


