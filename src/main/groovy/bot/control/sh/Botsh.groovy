/*
 * Copyright 2013 Matt Dean
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package bot.control.sh

import bot.Bot

import org.codehaus.groovy.tools.shell.*
import org.codehaus.groovy.tools.shell.util.*

// Import jline for history functions
//@Grab("jline:jline:2.10")

/**
 * A basic groovy shell for the bot
 * @author deanydean
 */
public class Botsh extends Groovysh {

    public static boolean PRINT_ERROR_STACKTRACES = false

    def lastResult = null

    private static Closure createDefaultRegistrar(){
        return { shell ->
            // Load the default commands and the botsh commands
            def r = new XmlCommandRegistrar(shell, classLoader)
            r.register(Groovysh.class.getResource("commands.xml"))
            r.register(Botsh.class.getResource("commands.xml"))
        }
    }

    public Botsh(){
        super(Thread.currentThread().contextClassLoader,
            generateBinding(null), new IO(System.in, System.out, System.err),
            createDefaultRegistrar())
    }

    public Botsh(binding){
        super(Thread.currentThread().contextClassLoader,
            generateBinding(binding), 
            new IO(System.in, System.out, System.err),
            createDefaultRegistrar())
    }

    public static generateBinding(binding){
        if(!binding)
            binding = new Binding()
        return binding
    }

    int run(final String[] args){
        io.setVerbosity(IO.Verbosity.QUIET)
        try{
            return super.run(args)
        }catch(e){
            Bot.LOG.error "${e.getMessage()}"
            Bot.LOG.debug "$e"
        }
    }

    public String renderPrompt(){
        return "BOT << "
    }

    public void displayError(e){
        Bot.LOG.error "$e"
        if(PRINT_ERROR_STACKTRACES)
            e.printStackTrace()
    }

    public void displayBuffer(ArrayList buffer){
        buffer.each {
            Bot.LOG.info "BUFFER> $it"
        }
    }

}
